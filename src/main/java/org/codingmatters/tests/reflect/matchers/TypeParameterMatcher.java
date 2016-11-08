package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.type.TypeParameterInfo;
import org.hamcrest.Matcher;

/**
 * Created by nelt on 11/8/16.
 */
public interface TypeParameterMatcher extends Matcher<TypeParameterInfo> {
    TypeParameterMatcher named(String name);

    TypeParameterMatcher upperBound(TypeMatcher... typeMatchers);

    TypeParameterMatcher lowerBound(TypeMatcher... typeMatchers);
}
