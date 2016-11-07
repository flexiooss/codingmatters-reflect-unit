package org.codingmatters.tests.reflect.matchers.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by nelt on 10/19/16.
 */
public class TypeWrapper {

    static public TypeWrapper wrap(Type type) {
        return new TypeWrapper(type);
    }

    // GenericArrayType, ParameterizedType, TypeVariable<D>, WildcardType

    private final Type type;

    private TypeWrapper(Type type) {
        this.type = type;
    }

    public String name() {
        if(this.type instanceof TypeVariable) {
            return ((TypeVariable) this.type).getName();
        } else {
            return this.type.getTypeName();
        }
    }

    public Type[] bounds() {
        if(this.type instanceof TypeVariable) {
            return ((TypeVariable)this.type).getBounds();
        } else {
            return new Type[0];
        }
    }

//    public boolean same(Type toType) {
//        if(this.type.equals(toType)) {
//            return true;
//        } else {
//
//        }
//    }
}
