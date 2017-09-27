package org.codingmatters.tests.compile.helpers.helpers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.codingmatters.tests.compile.helpers.helpers.invokers.InstanceMethodInvoker;

import java.lang.reflect.Method;

public class ObjectHelper {

    private final ClassLoaderHelper classLoader;
    private final Class clazz;
    private final Object o;

    public ObjectHelper(ClassLoaderHelper classLoader, Class as, Object o) {
        this.clazz = as;
        this.o = o;
        this.classLoader = classLoader;
    }

    public ObjectHelper as(ClassDescriptor toClass) {
        return this.as(toClass.className());
    }

    public ObjectHelper as(String toClass) {
        return this.classLoader.get(toClass).wrap(this.o);
    }

    public ObjectHelper as(Class toClass) {
        return new ObjectHelper(this.classLoader, toClass, this.o);
    }

    public ObjectHelper call(String methodName) {
        return this.call(methodName, (Class[]) null).with();
    }

    public InstanceMethodInvoker call(String methodName, ClassDescriptor ... argTypes) {
        return this.call(methodName, this.classLoader.resolve(argTypes));
    }

    public InstanceMethodInvoker call(String methodName, Class ... argTypes) {
        Method method;
        try {
            if(argTypes == null) {
                method = this.clazz.getMethod(methodName);
            } else {
                method = this.clazz.getMethod(methodName, argTypes);
            }
        } catch (NoSuchMethodException e) {
            throw new AssertionError("method " + methodName + " not found for class " + this.clazz, e);
        }
        return new InstanceMethodInvoker(this.classLoader, this, method);
    }

    public Object get() {
        return this.o;
    }

    public ObjectArrayHelper asArray() {
        return ObjectArrayHelper.from(this.classLoader, this);
    }

    @Override
    public String toString() {
        return "ObjectHelper{" +
                "clazz=" + clazz +
                ", o=" + o +
                '}';
    }
}
