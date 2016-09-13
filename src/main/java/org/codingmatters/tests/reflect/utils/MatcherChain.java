package org.codingmatters.tests.reflect.utils;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.LinkedList;

import static org.codingmatters.tests.reflect.utils.LambdaMatcher.match;

/**
 * Created by nelt on 9/11/16.
 */
public class MatcherChain<T> {

    private final LinkedList<Matcher<T>> matchers = new LinkedList<>();

    public void add(Matcher<T> m) {
        this.matchers.add(m);
    }

    public void addMatcher(String description, LambdaMatcher.Lambda<T> lambda) {
        this.matchers.add(match(description, lambda));
    }

    public Matcher<Object> compoundMatcher() {
        return Matchers.allOf(this.matchers.toArray(new Matcher[this.matchers.size()]));
    }

}
