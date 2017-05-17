package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Field;

/**
 * Created by nelt on 11/8/16.
 */
public interface FieldMatcher extends Matcher<Field> {
    FieldMatcher named(String name);

    FieldMatcher final_();

    FieldMatcher withType(Class type);
    FieldMatcher withType(TypeMatcher typeMatcher);
}
