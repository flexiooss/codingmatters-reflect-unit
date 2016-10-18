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
public class TypeMatcher extends TypeSafeMatcher<Type> {

    private final MatcherChain<Type> matchers = new MatcherChain<>();

    @Override
    protected boolean matchesSafely(Type item) {
        return matchers.compoundMatcher().matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("type variable");
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Type item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    public TypeMatcher named(String name) {
        this.matchers.addMatcher(
                "named " + name,
                item -> name.equals(this.typeName(item)),
                (item, description) -> description.appendText("was " + item.getTypeName())
        );
        return this;
    }

    public TypeMatcher withBound(Type type) {
        this.matchers.addMatcher("with bound " + type.getTypeName(),
                item -> Stream.of(item instanceof TypeVariable ? ((TypeVariable)item).getBounds() : new Type[0]).filter(t -> t.equals(type)).findFirst().isPresent(),
                (item, description) -> description.appendText("was " + Stream.of(item instanceof TypeVariable ? ((TypeVariable)item).getBounds() : new Type[0]).map(Type::getTypeName).collect(Collectors.joining(", ")))
        );
        return this;
    }

    private String typeName(Type type) {
        if (type instanceof TypeVariable) {
            return ((TypeVariable) type).getName();
        } else {
            return type.getTypeName();
        }
    }
}
