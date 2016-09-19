package org.codingmatters.tests.reflect;

/**
 * Created by nelt on 9/19/16.
 */
public class InstanceMatcher {
    public ClassMatcher class_() {
        return ClassMatcher.anInstanceClass();
    }

    public MethodMatcher method() {
        return MethodMatcher.anInstanceMethod();
    }

    public FieldMatcher field() {
        return FieldMatcher.anInstanceField();
    }

    public ClassMatcher interface_() {
        return ClassMatcher.anInstanceInterface();
    }
}
