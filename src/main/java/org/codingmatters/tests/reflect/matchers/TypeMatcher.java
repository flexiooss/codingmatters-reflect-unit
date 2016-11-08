package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Type;

/**
 * Created by nelt on 11/8/16.
 */
public interface TypeMatcher extends Matcher<Type> {
    TypeMatcher baseClass(Class aClass);

    TypeMatcher named(String name);

    TypeMatcher withParameterCount(int count);

    TypeMatcher withParameters(TypeParameterMatcher... parameterMatchers);
}
