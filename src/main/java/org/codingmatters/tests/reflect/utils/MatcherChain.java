package org.codingmatters.tests.reflect.utils;

import org.hamcrest.Matchers;

import java.util.LinkedList;

import static org.codingmatters.tests.reflect.utils.LambdaMatcher.match;

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
    public void addMatcher(String description, LambdaMatcher.Matcher<T> lambda, LambdaMatcher.MismatchDescripitor<T> mismatchDescription) {
        this.matchers.add(match(description, lambda, mismatchDescription));
    }

    public org.hamcrest.Matcher compoundMatcher() {
        return Matchers.allOf(this.matchers.toArray(new org.hamcrest.Matcher[this.matchers.size()]));
    }

}
