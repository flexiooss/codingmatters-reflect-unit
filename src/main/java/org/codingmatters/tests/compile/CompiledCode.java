package org.codingmatters.tests.compile;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;

import javax.tools.*;
import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by nelt on 9/6/16.
 */
public class CompiledCode {

    static public class Builder {
        List<URL> classpath = new LinkedList<>();
        List<File> sources = new LinkedList<>();

        public Builder classpath(URL ... elements) {
            if(elements != null) {
                for (URL element : elements) {
                    this.classpath.add(element);
                }
            }
            return this;
        }

        public Builder source(File ... elements) {
            if(elements != null) {
                for (File element : elements) {
                    this.sources.add(element);
                }
            }
            return this;
        }

        public CompiledCode compile() throws Exception {
            List<URL> urls = new ArrayList<>(this.classpath);

            for (File source : this.sources) {
                compileDir(source, urls);
                urls.add(source.toURI().toURL());
            }

            return new CompiledCode(URLClassLoader.newInstance(urls.toArray(new URL[urls.size()])));
        }
    }

    static public Builder builder() {
        return new CompiledCode.Builder();
    }

    static public URL findInClasspath(String pattern) throws MalformedURLException {
        for (String element : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
            if(element.matches(pattern)) {
                return new File(element).toURI().toURL();
            }
        }
        return null;
    }

    static public URL findLibraryInClasspath(String libraryName) throws MalformedURLException {
        /*
        jackson-core
        .*jackson-core/target/classes.*
        .*jackson-core-.*.jar
         */
        URL result = findMavenArtifactInClasspath(libraryName);
        if(result == null) {
            result = findMavenTargetClassesInClasspath(libraryName);
        }
        return result;
    }

    private static URL findMavenArtifactInClasspath(String libraryName) throws MalformedURLException {
        return findInClasspath(".*" + libraryName + "-.*.jar");
    }

    private static URL findMavenTargetClassesInClasspath(String libraryName) throws MalformedURLException {
        return findInClasspath(".*" + libraryName + "/target/classes.*");
    }

    static private CompiledCode compile(File dir, URLClassLoader classLoader) throws Exception {
        List<URL> urls = new LinkedList<>();
        if(classLoader != null) {
            Collections.addAll(urls, classLoader.getURLs());
        }
        compileDir(dir, urls);
        urls.add(dir.toURI().toURL());
        return new CompiledCode(URLClassLoader.newInstance(urls.toArray(new URL[urls.size()])));
    }

    private static void compileDir(File dir, List<URL> classLoaderUrls) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try(StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            fileManager.setLocation(StandardLocation.CLASS_PATH, toFileList(classLoaderUrls));
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(resolveJavaFiles(dir));
            DiagnosticCollector diagnosticListener = new DiagnosticCollector();
            JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnosticListener, null, null, compilationUnits);
            Boolean result = compilerTask.call();
            if(! result) {
                StringBuilder report = new StringBuilder();
                HashSet<JavaFileObject> sourceWithError = new HashSet<>();
                List<Diagnostic> diags = diagnosticListener.getDiagnostics();
                for (Diagnostic diag : diags) {
                    report.append(diag.toString()).append("\n");
                    if(diag.getSource() != null) {
                        sourceWithError.add((JavaFileObject) diag.getSource());
                    }
                }

                report.append("\nsource files with error :");
                for (JavaFileObject file : sourceWithError) {

                    report.append("\n").append(file.getName()).append(" : \n");
                    try(BufferedReader reader = new BufferedReader(file.openReader(true))) {
                        int index = 1;
                        for(String line = reader.readLine() ; line != null ; line = reader.readLine()) {
                            report.append(String.format("%03d   ", index)).append(line).append("\n");
                            index++;
                        }
                    }
                }


                throw new AssertionError(report);
            }
        }
    }

    private static List<File> toFileList(List<URL> classLoaderUrls) throws URISyntaxException {
        List<File> result = new LinkedList<>();
        for (URL classLoaderUrl : classLoaderUrls) {
            result.add(new File(classLoaderUrl.toURI()));
        }

        return result;
    }

    private static List<File> resolveJavaFiles(File dir) {
        List<File> results = new LinkedList<>();
        if(dir == null) return results;

        Collections.addAll(results, dir.listFiles(file -> file.getName().endsWith(".java")));
        for (File subDir : dir.listFiles(file -> file.isDirectory())) {
            results.addAll(resolveJavaFiles(subDir));
        }

        return results;
    }

    private final URLClassLoader classLoader;

    public CompiledCode(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoaderHelper classLoader() {
        return new ClassLoaderHelper(this.classLoader);
    }

    public CompiledCode withCompiled(File target) throws Exception {
        return compile(target, this.classLoader);
    }

    public Class getClass(String name) {
        try {
            return Class.forName(name, true, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public Invoker onClass(String className) {
        return new Invoker(this, this.getClass(className), null);
    }

    public Invoker on(Object o) {
        return new Invoker(this, o.getClass(), o);
    }


    static public class Invoker {
        private final CompiledCode compiledCode;
        private final Class aClass;
        private final Object on;

        private Invoker(CompiledCode compiledCode, Class aClass, Object on) {
            this.compiledCode = compiledCode;
            this.aClass = aClass;
            this.on = on;
        }

        public Invoker castedTo(String className) {
            return new Invoker(this.compiledCode, this.compiledCode.getClass(className), this.on);
        }

        public <T> T invoke(String method) throws Exception {
            try {
                return (T) this.aClass.getMethod(method).invoke(this.on);
            } catch(ClassCastException e) {
                throw new AssertionError(method + " return type mismatch", e);
            }
        }

        public ParametrizedInvoker invoke(String method, Class ... paramTypes) {
            return new ParametrizedInvoker(this.aClass, this.on, method, paramTypes);
        }

        static public class ParametrizedInvoker {

            private final Class aClass;
            private final Object on;
            private final String method;
            private final Class[] paramTypes;

            public ParametrizedInvoker(Class aClass, Object on, String method, Class[] paramTypes) {
                this.aClass = aClass;
                this.on = on;
                this.method = method;
                this.paramTypes = paramTypes;
            }

            public <T> T with(Object ... params) throws Exception {
                Method m = this.aClass.getMethod(this.method, this.paramTypes);
                try {
                    return (T) m.invoke(this.on, params);
                } catch(ClassCastException e) {
                    throw new AssertionError(m + " return type mismatch", e);
                } catch(IllegalArgumentException e) {
                    String msg = m + " called with wrong arguments: " + params;
                    if(params == null) {
                        msg += " (if you meant to invoke method with one null argument, call with new Object[] {null})";
                    }
                    throw new AssertionError(msg, e);
                }
            }
        }
    }

}
