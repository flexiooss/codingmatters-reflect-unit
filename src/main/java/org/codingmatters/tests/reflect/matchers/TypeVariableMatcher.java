package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nelt on 10/12/16.
 */
public class TypeVariableMatcher extends TypeSafeMatcher<TypeVariable> {

    private final MatcherChain<TypeVariable> matchers = new MatcherChain<>();

    @Override
    protected boolean matchesSafely(TypeVariable item) {
        return matchers.compoundMatcher().matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("type variable");
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(TypeVariable item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    public TypeVariableMatcher named(String name) {
        this.matchers.addMatcher("named " + name, item -> name.equals(item.getName()), item -> "was " + item.getName());
        return this;
    }

    public TypeVariableMatcher withBound(Type type) {
        this.matchers.addMatcher("with bound " + type.getTypeName(),
                item -> Stream.of(item.getBounds()).filter(t -> t.equals(type)).findFirst().isPresent(),
                item -> "was " + Stream.of(item.getBounds()).map(Type::getTypeName).collect(Collectors.joining(", ")));
        return this;
    }
}
