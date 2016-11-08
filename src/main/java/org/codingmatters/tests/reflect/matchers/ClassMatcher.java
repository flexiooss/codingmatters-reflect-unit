package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

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

    ClassMatcher extending(Class aClass);
}
