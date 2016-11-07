package org.codingmatters.tests.reflect.matchers.support.utils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

/**
 * Created by nelt on 10/12/16.
 */
public class CollectorMatcher<T, E> extends TypeSafeMatcher<E> {

    private final Matcher<T> methodMatcher;
    private final ElementCollector<T, E> elementCollector;

    public CollectorMatcher(Matcher<T> methodMatcher, ElementCollector<T, E> elementCollector) {
        this.methodMatcher = methodMatcher;
        this.elementCollector = elementCollector;
    }

    @Override
    protected boolean matchesSafely(E item) {
        for (T member : this.elementCollector.candidates(item)) {
            if(this.methodMatcher.matches(member)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        this.methodMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(E item, Description mismatchDescription) {
        mismatchDescription.appendText("not found");
    }

    public interface ElementCollector<T, E> {
        List<T> candidates(E item);
    }
}
