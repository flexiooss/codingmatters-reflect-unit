package org.codingmatters.tests.reflect.matchers.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by nelt on 10/20/16.
 */
public class TypeInfo {


    public static TypeInfo from(Type type) {
        if(type instanceof Class) {
            return fromClass((Class) type);
        } else if(type instanceof ParameterizedType) {
            return fromParametrizedType((ParameterizedType) type);
        } else if(type instanceof TypeVariable) {
            return fromTypeVariable((TypeVariable) type);
        } else {
            throw new RuntimeException("NYIMPL : type info for " + type.getTypeName() + " - " + type.getClass().getName());
        }
    }

    private static HashMap<Class, TypeInfo> classTypeInfoCache = new HashMap<>();

    private static TypeInfo fromClass(Class aClass) {
        synchronized (classTypeInfoCache) {
            if(! classTypeInfoCache.containsKey(aClass)) {
                boolean generic = aClass.getTypeParameters().length > 0;
                List<TypeParameterInfo> parameters = new ArrayList<>(aClass.getTypeParameters().length);
                for (TypeVariable variable : aClass.getTypeParameters()) {
                    parameters.add(TypeParameterInfo.from(variable));
                }
                classTypeInfoCache.put(aClass, new TypeInfo(aClass, aClass.getName(), generic, false, parameters));
            }

            return classTypeInfoCache.get(aClass);
        }
    }

    private static TypeInfo fromParametrizedType(ParameterizedType type) {
        List<TypeParameterInfo> parameters = new ArrayList<>();
        for (Type parameter : type.getActualTypeArguments()) {
            parameters.add(TypeParameterInfo.from(parameter));
        }

        return new TypeInfo(
                (Class) type.getRawType(),
                type.getRawType().getTypeName(),
                true, false,
                parameters);
    }

    private static TypeInfo fromTypeVariable(TypeVariable type) {
        return new TypeInfo(null, type.getTypeName(), false, true, new ArrayList<>());
    }

    private final Class baseClass;
    private final String name;
    private final boolean generic;
    private final boolean variable;
    private final List<TypeParameterInfo> parameters;

    private TypeInfo(
            Class baseClass,
            String name,
            boolean generic,
            boolean variable,
            List<TypeParameterInfo> parameters) {
        this.baseClass = baseClass;
        this.name = name;
        this.variable = variable;
        this.generic = generic;
        this.parameters = parameters;
    }

    public Class baseClass() {
        return baseClass;
    }

    public boolean isGeneric() {
        return this.generic;
    }

    public boolean isVariable() {
        return this.variable;
    }

    public String name() {
        return this.name;
    }

    public List<TypeParameterInfo> parameters() {
        return this.parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeInfo typeInfo = (TypeInfo) o;
        return generic == typeInfo.generic &&
                variable == typeInfo.variable &&
                Objects.equals(name, typeInfo.name) &&
                Objects.equals(parameters, typeInfo.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, generic, variable, parameters);
    }

    @Override
    public String toString() {
        return "TypeInfo{" +
                "baseClass=" + baseClass +
                ", name='" + name + '\'' +
                ", generic=" + generic +
                ", variable=" + variable +
                ", parameters=" + parameters +
                '}';
    }
}
