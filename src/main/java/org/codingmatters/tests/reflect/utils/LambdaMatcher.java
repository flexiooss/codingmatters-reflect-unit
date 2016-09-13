package org.codingmatters.tests.reflect.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 9/8/16.
 */
public class LambdaMatcher<T> extends TypeSafeMatcher<T> {

    static public <T> LambdaMatcher<T> match(String description, Lambda<T> lamda) {
        return new LambdaMatcher(description, lamda);
    }

    private final String description;
    private final Lambda lambda;

    public LambdaMatcher(String description, Lambda<T> lambda) {
        this.description = description;
        this.lambda = lambda;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    @Override
    protected boolean matchesSafely(T item) {
        return this.lambda.matches(item);
    }

    public interface Lambda<T> {
        boolean matches(T item);
    }
}
