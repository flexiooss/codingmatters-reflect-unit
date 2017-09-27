package org.codingmatters.tests.compile.helpers.helpers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.codingmatters.tests.compile.helpers.helpers.invokers.ClassMethodInvoker;
import org.codingmatters.tests.compile.helpers.helpers.invokers.ConstructorInvoker;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ClassHelper {
    private final ClassLoaderHelper classLoader;
    private final Class clazz;

    public ClassHelper(ClassLoaderHelper classLoader, Class clazz) {
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    public ObjectHelper newInstance() {
        return this.newInstance((Class[]) null).with();
    }

    public ConstructorInvoker newInstance(ClassDescriptor ... argTypeDescriptors) {
        return this.newInstance(this.classLoader.resolve(argTypeDescriptors));
    }

    public ConstructorInvoker newInstance(Class ... argTypes) {
        Constructor constructor;
        try {
            if (argTypes != null) {
                constructor = clazz.getConstructor(argTypes);
            } else {
                constructor = clazz.getConstructor();
            }
        } catch (NoSuchMethodException e) {
            if( argTypes == null) {
                throw new AssertionError("no default constructor for class " + clazz.getName(), e);
            } else {
                throw new AssertionError("no constructor for class " + clazz.getName() + " with args types " + Arrays.asList(argTypes), e);
            }
        }
        return new ConstructorInvoker(this.classLoader, constructor);
    }

    public ObjectHelper wrap(Object o) {
        return new ObjectHelper(this.classLoader, this.clazz, o);
    }

    public ObjectHelper call(String method) {
        return this.call(method, null).with();
    }

    public ClassMethodInvoker call(String method, Class ... argTypes) {
        try {
            if(argTypes == null) {
                return new ClassMethodInvoker(this.classLoader, this.clazz.getMethod(method));
            } else {
                return new ClassMethodInvoker(this.classLoader, this.clazz.getMethod(method, argTypes));
            }
        } catch (NoSuchMethodException e) {
            if(argTypes == null) {
                throw new AssertionError("no class method " + method + " on class " + this.clazz.getName(), e);
            } else {
                throw new AssertionError("no class method " + method + " on class " + this.clazz.getName() + " with args types " + Arrays.asList(argTypes), e);
            }
        }
    }

    public Class get() {
        return this.clazz;
    }
}
