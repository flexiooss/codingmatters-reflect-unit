package org.codingmatters.tests.compile;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nelt on 9/6/16.
 */
public class CompiledCode {

    static public CompiledCode compile(File dir) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try(StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {

            Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(resolveJavaFiles(dir));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits1);
            task.call();
        }
        return new CompiledCode(URLClassLoader.newInstance(new URL[] {dir.toURI().toURL()}));
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
