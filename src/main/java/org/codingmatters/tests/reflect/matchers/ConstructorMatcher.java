package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Constructor;

/**
 * Created by nelt on 11/8/16.
 */
public interface ConstructorMatcher extends Matcher<Constructor> {
    ConstructorMatcher withParameters(Class... parameters);
}
