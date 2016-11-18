package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * Created by nelt on 11/8/16.
 */
public interface ConstructorMatcher extends Matcher<Constructor> {
    ConstructorMatcher withParameters(Class... parameters);
    ConstructorMatcher withParameters(Matcher<Type>... typeMatcher);
}
