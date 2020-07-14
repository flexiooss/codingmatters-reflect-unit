package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Type;

/**
 * Created by nelt on 11/8/16.
 */
public interface ClassMatcher extends Matcher<Class> {
    ClassMatcher named(String name);

    ClassMatcher with(MethodMatcher methodMatcher);

    ClassMatcher with(FieldMatcher fieldMatcher);

    ClassMatcher withParameter(TypeMatcher typeMatcher);

    ClassMatcher with(ConstructorMatcher constructorMatcher);

    ClassMatcher final_();

    ClassMatcher implementing(Class interfaceClass);
    ClassMatcher implementing(Matcher<Type> interfaceMatcher);

    ClassMatcher extending(Class aClass);
}
