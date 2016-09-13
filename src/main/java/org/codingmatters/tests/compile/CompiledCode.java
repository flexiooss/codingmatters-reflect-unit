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

}
