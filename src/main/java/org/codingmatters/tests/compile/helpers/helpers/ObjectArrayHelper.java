package org.codingmatters.tests.compile.helpers.helpers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;

import java.lang.reflect.Array;

public class ObjectArrayHelper {
    static public ObjectArrayHelper from(ClassLoaderHelper classLoader, ObjectHelper o) {
        if(o.get() instanceof Object[]) {
            ObjectHelper[] os = new ObjectHelper[Array.getLength(o.get())];
            for(int i = 0 ; i < Array.getLength(o.get()) ; i++) {
                os[i] = classLoader.wrap(Array.get(o.get(), i));
            }
            return new ObjectArrayHelper(classLoader, os);
        } else {
            throw new AssertionError("object is not an array " + o);
        }
    }

    private final ClassLoaderHelper classLoader;
    private final ObjectHelper[] os;

    public ObjectArrayHelper(ClassLoaderHelper classLoader, ObjectHelper[] os) {
        this.classLoader = classLoader;
        this.os = os;
    }

    public ObjectHelper get(int i) {
        return this.os[i];
    }

    public Object [] get() {
        if(this.os == null) return null;
        Object[] results = new Object[this.os.length];
        for (int i = 0; i < this.os.length; i++) {
            results[i] = this.os[i].get();
        }
        return results;
    }
}
