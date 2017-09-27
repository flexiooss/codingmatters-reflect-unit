package org.codingmatters.tests.compile.helpers.helpers.invokers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.codingmatters.tests.compile.helpers.helpers.ObjectHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ConstructorInvoker {

    private final ClassLoaderHelper classLoader;
    private final Constructor constructor;

    public ConstructorInvoker(ClassLoaderHelper classLoader, Constructor constructor) {
        this.classLoader = classLoader;
        this.constructor = constructor;
    }

    public ObjectHelper with(Object ... args) {
        try {
            if (args == null) {
                return this.classLoader.wrap(this.constructor.newInstance());
            } else {
                return this.classLoader.wrap(this.constructor.newInstance(args));
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if(args == null) {
                throw new AssertionError("error calling default constructor " + this.constructor, e);
            } else {
                throw new AssertionError("error calling constructor " + this.constructor + " with args " + Arrays.asList(args), e);
            }
        }
    }

}
