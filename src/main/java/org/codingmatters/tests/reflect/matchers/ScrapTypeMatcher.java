package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.internal.TypeWrapper;
import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nelt on 10/12/16.
 */
public class ScrapTypeMatcher extends TypeSafeMatcher<Type> {

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

    public ScrapTypeMatcher named(String name) {
        this.matchers.addMatcher(
                "named " + name,
                item -> name.equals(TypeWrapper.wrap(item).name()),
                (item, description) -> description.appendText("was " + TypeWrapper.wrap(item).name())
        );
        return this;
    }

    public ScrapTypeMatcher withBound(Type aType) {
        TypeWrapper type = TypeWrapper.wrap(aType);
        this.matchers.addMatcher("with bound " + type.name(),
                item -> Stream.of(TypeWrapper.wrap(item).bounds()).filter(t -> t.equals(aType)).findFirst().isPresent(),
                (item, description) -> description.appendText("was " + Stream.of(TypeWrapper.wrap(item).bounds()).map(Type::getTypeName).collect(Collectors.joining(", ")))
        );
        return this;
    }
}
