package org.codingmatters.tests.reflect.matchers.support.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 9/8/16.
 */
public class LambdaMatcher<T> extends TypeSafeMatcher<T> {


    static public <T> LambdaMatcher<T> match(String description, Matcher<T> lamda) {
        return match(description, lamda, null);
    }
    static public <T> LambdaMatcher<T> match(String description, Matcher<T> lamda, ItemDescripitor<T> mismatchDescripitor) {
        return new LambdaMatcher(description, lamda, mismatchDescripitor);
    }
    static public <T> LambdaMatcher<T> match(Descripitor descriptor, Matcher<T> lamda) {
        return match(descriptor, lamda);
    }
    static public <T> LambdaMatcher<T> match(Descripitor descriptor, Matcher<T> lamda, ItemDescripitor<T> mismatchDescripitor) {
        return new LambdaMatcher(descriptor, lamda, mismatchDescripitor);
    }

    private final Descripitor descriptor;
    private final Matcher lambda;
    private final ItemDescripitor<T> mismatchDescripitor;

    public LambdaMatcher(Descripitor descripitor, Matcher<T> lambda, ItemDescripitor<T> mismatchDescripitor) {
        this.descriptor = descripitor;
        this.lambda = lambda;
        this.mismatchDescripitor = mismatchDescripitor;
    }

    public LambdaMatcher(String description, Matcher<T> lambda, ItemDescripitor<T> mismatchDescripitor) {
        this(desc -> desc.appendText(description), lambda, mismatchDescripitor);
    }

    @Override
    public void describeTo(Description description) {
        this.descriptor.describe(description);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        if(this.mismatchDescripitor != null) {
            this.mismatchDescripitor.describe(item, mismatchDescription);
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

    public interface Descripitor {
        void describe(Description description);
    }

    public interface ItemDescripitor<T> {
        void describe(T item, Description description);
    }
}
