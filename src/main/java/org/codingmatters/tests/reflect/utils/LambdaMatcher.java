package org.codingmatters.tests.reflect.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 9/8/16.
 */
public class LambdaMatcher<T> extends TypeSafeMatcher<T> {


    static public <T> LambdaMatcher<T> match(String description, Matcher<T> lamda) {
        return match(description, lamda, null);
    }
    static public <T> LambdaMatcher<T> match(String description, Matcher<T> lamda, MismatchDescripitor<T> mismatchDescripitor) {
        return new LambdaMatcher(description, lamda, mismatchDescripitor);
    }

    private final String description;
    private final Matcher lambda;
    private final MismatchDescripitor<T> mismatchDescripitor;

    public LambdaMatcher(String description, Matcher<T> lambda, MismatchDescripitor<T> mismatchDescripitor) {
        this.description = description;
        this.lambda = lambda;
        this.mismatchDescripitor = mismatchDescripitor;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        if(this.mismatchDescripitor != null) {
            mismatchDescription.appendText(this.mismatchDescripitor.describe(item));
        } else {
            super.describeMismatchSafely(item, mismatchDescription);
        }
    }

    @Override
    protected boolean matchesSafely(T item) {
        return this.lambda.matches(item);
    }

    public interface Matcher<T> {
        boolean matches(T item);
    }

    public interface MismatchDescripitor<T> {
        String describe(T item);
    }
}
