package org.codingmatters.tests.reflect.matchers.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelt on 10/21/16.
 */
public class TypeParameterInfo {

    public static TypeParameterInfo from(Type type) {
        if(type instanceof TypeVariable) {
            return fromTypeVariable((TypeVariable) type);
        } else if(type instanceof Class) {
            return fromClass((Class) type);
        } else if(type instanceof WildcardType) {
            return fromWildcard((WildcardType) type);
        } else if(type instanceof ParameterizedType) {
            return fromParameterizedType((ParameterizedType) type);
        } else {
            throw new RuntimeException("NYIMPL type parameter info from : " + type + " (" + type.getClass().getName() + ")");
        }
    }

    private static TypeParameterInfo fromTypeVariable(TypeVariable type) {
        return new TypeParameterInfo(type.getName(), null, boundsFrom(type.getBounds()), null, null);
    }

    private static TypeParameterInfo fromClass(Class type) {
        return new TypeParameterInfo(type.getName(), TypeInfo.from(type), null, null, type);
    }

    private static TypeParameterInfo fromWildcard(WildcardType type) {
        return new TypeParameterInfo("?", null, boundsFrom(type.getUpperBounds()), boundsFrom(type.getLowerBounds()), null);
    }

    private static TypeParameterInfo fromParameterizedType(ParameterizedType type) {
        return new TypeParameterInfo(type.getTypeName(), TypeInfo.from(type), null, null, null);
    }

    private static ArrayList<TypeInfo> boundsFrom(Type[] typeBounds) {
        ArrayList<TypeInfo> bounds = new ArrayList<>(typeBounds.length);
        for (Type bound : typeBounds) {
            bounds.add(TypeInfo.from(bound));
        }
        return bounds;
    }

    private final String name;
    private final TypeInfo type;
    private final List<TypeInfo> upperBounds;
    private final List<TypeInfo> lowerBounds;
    private final Class clazz;

    private TypeParameterInfo(String name, TypeInfo type, List<TypeInfo> upperBounds, List<TypeInfo> lowerBounds, Class clazz) {
        this.name = name;
        this.type = type;
        this.upperBounds = upperBounds != null ? upperBounds : new ArrayList<>(0);
        this.lowerBounds = lowerBounds != null ? lowerBounds : new ArrayList<>(0);
        this.clazz = clazz;
    }

    public String name() {
        return this.name;
    }

    public TypeInfo type() {
        return type;
    }

    public List<TypeInfo> upperBounds() {
        return this.upperBounds;
    }

    public List<TypeInfo> lowerBounds() {
        return lowerBounds;
    }

    public boolean isWildcard() {
        return this.name.equals("?");
    }

    public Class clazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return "TypeParameterInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", upperBounds=" + upperBounds +
                ", lowerBounds=" + lowerBounds +
                ", clazz=" + clazz +
                '}';
    }
}
