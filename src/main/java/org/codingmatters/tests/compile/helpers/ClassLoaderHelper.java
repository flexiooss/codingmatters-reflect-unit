package org.codingmatters.tests.compile.helpers;

import org.codingmatters.tests.compile.helpers.helpers.ClassDescriptor;
import org.codingmatters.tests.compile.helpers.helpers.ClassHelper;
import org.codingmatters.tests.compile.helpers.helpers.ObjectHelper;

public class ClassLoaderHelper {

    static public ClassDescriptor c(Class clazz) {
        return c(clazz.getName());
    }
    static public ClassDescriptor c(String className) {
        return new ClassDescriptor(className);
    }

    static public ClassLoaderHelper current() {
        return new ClassLoaderHelper(Thread.currentThread().getContextClassLoader());
    }

    private final ClassLoader classLoader;

    public ClassLoaderHelper(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassHelper get(ClassDescriptor classDescriptor) {
        return this.get(classDescriptor.className());
    }

    public ClassHelper get(String className) {
        try {
            return new ClassHelper(this, this.classForName(className));
        } catch (ClassNotFoundException e) {
            throw new AssertionError("class " + className + " not found in class loader", e);
        }
    }

    public ObjectHelper wrap(Object o) {
        return new ObjectHelper(this, o.getClass(), o);
    }

    public Class[] resolve(ClassDescriptor[] descriptors) {
        Class[] results = null;
        if(descriptors != null) {
            results = new Class[descriptors.length];
            for(int i = 0 ; i < descriptors.length ; i++) {
                try {
                    results[i] = this.classForName(descriptors[i].className());
                } catch (ClassNotFoundException e) {
                    throw new AssertionError("not class def for " + i + "th class descriptor " + descriptors[i], e);
                }
            }
        }
        return results;
    }

    private Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className, true, this.classLoader);
    }
}
