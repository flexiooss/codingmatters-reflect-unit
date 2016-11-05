package org.codingmatters.tests.reflect.matchers.internal;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeInfoMatcher extends TypeSafeMatcher<TypeInfo> {

    public static TypeInfoMatcher generic() {
        return new TypeInfoMatcher().generic_();
    }

    public static TypeInfoMatcher nonGeneric() {
        return new TypeInfoMatcher().nonGeneric_();
    }

    public static VariableTypeMatcher variable() {
        return new VariableTypeMatcher();
    }

    public static TypeInfoMatcher class_(Class aClass) {
        return new TypeInfoMatcher().baseClass(aClass);
    }


    private final MatcherChain<TypeInfo> matchers = new MatcherChain<>();

    private TypeInfoMatcher() {
    }

    private TypeInfoMatcher generic_() {
        this.matchers.addMatcher(
                description -> description.appendText("a generic type"),
                item -> item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not generic"));
        return this;
    }

    private TypeInfoMatcher nonGeneric_() {
        this.matchers.addMatcher(
                description -> description.appendText("a non generic type"),
                item -> ! item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was generic"));
        return this;
    }

    public TypeInfoMatcher baseClass(Class aClass) {
        this.matchers.addMatcher(
                description -> description.appendText("base class ").appendValue(aClass),
                item -> aClass.equals(item.baseClass()),
                (item, description) -> description.appendText("was ").appendValue(item.baseClass())
        );
        return this;
    }


    public TypeInfoMatcher withParameterCount(int count) {
        this.matchers.addMatcher(
                description -> description.appendText("has " + count + " parameters"),
                item -> item.parameters().size() == count,
                (item, description) -> description.appendText("had " + item.parameters().size() + " parameters")
        );
        return this;
    }

    public TypeInfoMatcher withParameters(TypeParameterMatcher ... parameterMatchers) {
        for(int i = 0 ; i < parameterMatchers.length ; i++) {
            int paramIndex = i;
            this.matchers.addMatcher(
                    description -> {
                        description.appendText("parameter " + paramIndex);
                        parameterMatchers[paramIndex].describeTo(description);
                    },
                    item -> parameterMatchers[paramIndex].matches(item.parameters().size() > paramIndex ? item.parameters().get(paramIndex) : null),
                    (item, description) -> parameterMatchers[paramIndex].describeMismatch(
                            item.parameters().size() > paramIndex ? item.parameters().get(paramIndex) : null,
                            description)
            );
        }
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
