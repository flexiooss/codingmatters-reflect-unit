package org.codingmatters.tests.reflect.matchers.support;

import org.hamcrest.Matchers;

import java.util.LinkedList;

/**
 * Created by nelt on 9/11/16.
 */
public class MatcherChain<T> {

    private final LinkedList<org.hamcrest.Matcher> matchers = new LinkedList<>();

    public void add(org.hamcrest.Matcher m) {
        this.matchers.add(m);
    }

    public void addMatcher(String description, LambdaMatcher.Matcher<T> lambda) {
        this.addMatcher(description, lambda, null);
    }
    public void addMatcher(String description, LambdaMatcher.Matcher<T> lambda, LambdaMatcher.ItemDescripitor<T> mismatchDescription) {
        this.matchers.add(LambdaMatcher.match(description, lambda, mismatchDescription));
    }
    public void addMatcher(LambdaMatcher.Descripitor descriptor, LambdaMatcher.Matcher<T> lambda) {
        this.addMatcher(descriptor, lambda, null);
    }
    public void addMatcher(LambdaMatcher.Descripitor descriptor, LambdaMatcher.Matcher<T> lambda, LambdaMatcher.ItemDescripitor<T> mismatchDescription) {
        this.matchers.add(LambdaMatcher.match(descriptor, lambda, mismatchDescription));
    }

    public org.hamcrest.Matcher compoundMatcher() {
        return Matchers.allOf(this.matchers.toArray(new org.hamcrest.Matcher[this.matchers.size()]));
    }

}
