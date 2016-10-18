package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by nelt on 10/14/16.
 */
public class GenericTypeMatcher extends TypeSafeMatcher<Type> {
    private final MatcherChain<Type> matchers = new MatcherChain<>();

    public GenericTypeMatcher() {
        this.matchers.addMatcher("generic",
                item -> this.isGeneric(item),
                (item, description) -> description.appendText(item.getTypeName() + " is not generic")
        );
    }

    private boolean isGeneric(Type item) {
        if(item instanceof Class) {
            return Stream.of(((Class) item).getTypeParameters()).count() > 0;
        } else if(item instanceof ParameterizedType) {
            return true;
        } else {
            return false;
        }
    }

    public GenericTypeMatcher of(Class aClass) {
        this.matchers.addMatcher("of " + aClass.getName(),
                item -> item.equals(aClass),
                (item, description) -> description.appendText("was of " + item.getTypeName())
        );
        return this;
    }

    public GenericTypeMatcher with(TypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Type>(
                typeMatcher,
                item -> Arrays.asList(item instanceof Class ? ((Class)item).getTypeParameters() : new Type[0])
        ));
        return this;
    }


    @Override
    protected boolean matchesSafely(Type aClass) {
        return matchers.compoundMatcher().matches(aClass);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Type item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }
}
