package org.codingmatters.tests.reflect;

/**
 * Created by nelt on 9/8/16.
 */
public class ReflectMatchers {
    static public ClassMatcher aClass() {
        return ClassMatcher.aClass();
    }

    static public ClassMatcher anInterface() {
        return ClassMatcher.anInterface();
    }

    static public ConstructorMatcher aConstructor() {
        return ConstructorMatcher.aConstructor();
    }

    static public MethodMatcher aMethod() {
        return MethodMatcher.aMethod();
    }

    static public MethodMatcher aStaticMethod() {
        return MethodMatcher.aStaticMethod();
    }

    static public MethodMatcher anInstanceMethod() {
        return MethodMatcher.anInstanceMethod();
    }

    static public FieldMatcher aField() {
        return FieldMatcher.aField();
    }

    static public FieldMatcher aStaticField() {
        return FieldMatcher.aStaticField();
    }

    static public FieldMatcher anInstanceField() {
        return FieldMatcher.anInstanceField();
    }
}
