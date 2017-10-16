package org.codingmatters.tests.compile.helpers.helpers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;

import java.lang.reflect.Array;

public class ClassArrayHelper extends ClassHelper {
    private final Class elementClazz;

    public ClassArrayHelper(ClassLoaderHelper classLoader, Class clazz) {
        super(classLoader, Array.newInstance(clazz, 0).getClass());
        this.elementClazz = clazz;
    }

    public ObjectHelper newArray(Object ... elements) {
        if(elements == null) throw new NullPointerException();
        Object result = Array.newInstance(this.elementClazz, elements.length);
        for (int i = 0; i < elements.length; i++) {
            Array.set(result, i, elements[i]);
        }
        return new ObjectHelper(this.classLoader(), this.get(), result);
    }
}
