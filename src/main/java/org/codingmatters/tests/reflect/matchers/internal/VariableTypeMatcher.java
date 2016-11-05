package org.codingmatters.tests.reflect.matchers.internal;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 10/26/16.
 */
public class VariableTypeMatcher extends TypeSafeMatcher<TypeInfo> {

    private final MatcherChain<TypeInfo> matchers = new MatcherChain<>();

    public VariableTypeMatcher() {
        this.matchers.addMatcher(
                description -> description.appendText("a variable type"),
                item -> item.isVariable(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not a variable")
        );
    }

    public VariableTypeMatcher named(String name) {
        this.matchers.addMatcher(
                description -> description.appendText("named ").appendValue(name),
                item -> name.equals(item.name()),
                (item, description) -> description.appendText("name was ").appendValue(item.name())
        );
        return this;
    }


    @Override
    protected boolean matchesSafely(TypeInfo item) {
        return matchers.compoundMatcher().matches(item);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(TypeInfo item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }
}
