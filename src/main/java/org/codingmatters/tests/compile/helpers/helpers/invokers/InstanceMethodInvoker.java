package org.codingmatters.tests.compile.helpers.helpers.invokers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.codingmatters.tests.compile.helpers.helpers.ObjectHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceMethodInvoker {
    private final ClassLoaderHelper classLoader;
    private final ObjectHelper objectHelper;
    private final Method method;

    public InstanceMethodInvoker(ClassLoaderHelper classLoader, ObjectHelper objectHelper, Method method) {
        this.classLoader = classLoader;
        this.objectHelper = objectHelper;
        this.method = method;
    }

    public ObjectHelper with(Object ... args) {
        Object result;
        try {
            if(args == null) {
                result = this.method.invoke(this.objectHelper.get());
            } else {
                result = this.method.invoke(this.objectHelper.get(), args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError("failed invoking method " + this.method.getName(), e);
        }
        return this.classLoader.wrap(result);
    }
}
