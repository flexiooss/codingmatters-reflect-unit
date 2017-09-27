package org.codingmatters.tests.compile.helpers.helpers.invokers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.codingmatters.tests.compile.helpers.helpers.ObjectHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassMethodInvoker {
    private final ClassLoaderHelper classLoader;
    private final Method method;

    public ClassMethodInvoker(ClassLoaderHelper classLoader, Method method) {
        this.classLoader = classLoader;
        this.method = method;
    }

    public ObjectHelper with(Object ... args) {
        Object result;
        try {
            if(args == null) {
                result = this.method.invoke(null);
            } else {
                result = this.method.invoke(null, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError("failed invoking method " + this.method.getName(), e);
        }
        return this.classLoader.wrap(result);
    }
}
