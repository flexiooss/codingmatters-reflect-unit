package org.codingmatters.tests.compile;

import javax.tools.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nelt on 9/6/16.
 */
public class CompiledCode {

    static public CompiledCode compile(File dir) throws Exception {
        return compile(dir, null);
    }

    static private CompiledCode compile(File dir, URLClassLoader classLoader) throws Exception {
        List<URL> urls = new LinkedList<>();
        if(classLoader != null) {
            for (URL url : classLoader.getURLs()) {
                urls.add(url);
            }
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
                for (Object o : diagnosticListener.getDiagnostics()) {
                    report.append(o.toString()).append("\n");
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
        for (File javaFile : dir.listFiles(file -> file.getName().endsWith(".java"))) {
            results.add(javaFile);
        }
        for (File subDir : dir.listFiles(file -> file.isDirectory())) {
            results.addAll(resolveJavaFiles(subDir));
        }

        return results;
    }

    private final URLClassLoader classLoader;

    public CompiledCode(URLClassLoader classLoader) {
        this.classLoader = classLoader;
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
                try {
                    return (T) this.aClass.getMethod(this.method, this.paramTypes).invoke(this.on, params);
                } catch(ClassCastException e) {
                    throw new AssertionError(method + " return type mismatch", e);
                }
            }
        }
    }

}
