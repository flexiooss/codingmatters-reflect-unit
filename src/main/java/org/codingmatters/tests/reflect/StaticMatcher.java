package org.codingmatters.tests.reflect;

/**
 * Created by nelt on 9/19/16.
 */
public class StaticMatcher {

    public ClassMatcher class_() {
        return ClassMatcher.aStaticClass();
    }

    public MethodMatcher method() {
        return MethodMatcher.aStaticMethod();
    }

    public FieldMatcher field() {
        return FieldMatcher.aStaticField();
    }

    public ClassMatcher interface_() {
        return ClassMatcher.aStaticInterface();
    }
}
