package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.support.MatcherChain;
import org.codingmatters.tests.reflect.matchers.type.TypeParameterInfo;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeParameterMatcher extends TypeSafeMatcher<TypeParameterInfo> {

    static public TypeParameterMatcher typeParameter() {
        return new TypeParameterMatcher();
    }

    private final MatcherChain<TypeParameterInfo> matchers = new MatcherChain<>();

    private TypeParameterMatcher() {}

    public TypeParameterMatcher named(String name) {
        this.matchers.addMatcher(
                description -> description.appendText("named ").appendValue(name),
                item -> name.equals(item.name()),
                (item, description) -> description.appendText("was ").appendValue(item.name()));
        return this;
    }

    public TypeParameterMatcher upperBound(TypeMatcher... typeMatchers) {
        for(int i = 0; i < typeMatchers.length ; i++) {
            int index = i;
            this.matchers.addMatcher(
                    description -> {
                        description.appendText("with upper bound " + index + " ");
                        typeMatchers[index].describeTo(description);
                    },
                    item -> typeMatchers[index].matches(item.upperBounds().get(index)),
                    (item, description) -> {
                        description.appendText("was ");
                        typeMatchers[index].describeMismatch(item.upperBounds().get(index), description);
                    });
        }
        return this;
    }

    public TypeParameterMatcher lowerBound(TypeMatcher... typeMatchers) {
        for(int i = 0; i < typeMatchers.length ; i++) {
            int index = i;
            this.matchers.addMatcher(
                    description -> {
                        description.appendText("with lower bound " + index + " ");
                        typeMatchers[index].describeTo(description);
                    },
                    item -> typeMatchers[index].matches(item.lowerBounds().get(index)),
                    (item, description) -> {
                        description.appendText("was ");
                        typeMatchers[index].describeMismatch(item.lowerBounds().get(index), description);
                    });
        }
        return this;
    }

    @Override
    protected boolean matchesSafely(TypeParameterInfo item) {
        return matchers.compoundMatcher().matches(item);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(TypeParameterInfo item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }
}
