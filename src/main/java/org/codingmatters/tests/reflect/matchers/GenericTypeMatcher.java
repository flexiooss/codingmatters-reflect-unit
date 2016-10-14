package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by nelt on 10/14/16.
 */
public class GenericTypeMatcher extends TypeSafeMatcher<Class> {
    private final MatcherChain<Class> matchers = new MatcherChain<>();

    public GenericTypeMatcher() {
        this.matchers.addMatcher("generic",
                item -> this.isGeneric(item),
                item -> item.getName() + " is not generic");
    }

    private boolean isGeneric(Class item) {
        return Stream.of(item.getTypeParameters()).count() > 0;
    }

    public GenericTypeMatcher with(TypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Class>(
                typeMatcher,
                item -> Arrays.asList(item.getTypeParameters())
        ));
        return this;
    }


    @Override
    protected boolean matchesSafely(Class aClass) {
        return matchers.compoundMatcher().matches(aClass);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Class item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    public GenericTypeMatcher of(Class aClass) {
        this.matchers.addMatcher("of " + aClass.getName(),
                item -> item.equals(aClass),
                item -> "was of " + item.getName());
        return this;
    }
}
