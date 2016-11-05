package org.codingmatters.tests.reflect.matchers.internal;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.lang.reflect.Type;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeMatcher extends BaseMatcher<Type> {

    public static TypeMatcher generic() {
        return new TypeMatcher().generic_();
    }

    public static TypeMatcher nonGeneric() {
        return new TypeMatcher().nonGeneric_();
    }

    public static VariableTypeMatcher variable() {
        return new VariableTypeMatcher();
    }

    public static TypeMatcher class_(Class aClass) {
        return new TypeMatcher().baseClass(aClass);
    }

    private final MatcherChain<TypeInfo> matchers = new MatcherChain<>();

    private TypeMatcher() {
    }

    private TypeMatcher generic_() {
        this.matchers.addMatcher(
                description -> description.appendText("a generic type"),
                item -> item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not generic"));
        return this;
    }

    private TypeMatcher nonGeneric_() {
        this.matchers.addMatcher(
                description -> description.appendText("a non generic type"),
                item -> ! item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was generic"));
        return this;
    }

    public TypeMatcher baseClass(Class aClass) {
        this.matchers.addMatcher(
                description -> description.appendText("base class ").appendValue(aClass),
                item -> aClass.equals(item.baseClass()),
                (item, description) -> description.appendText("was ").appendValue(item.baseClass())
        );
        return this;
    }


    public TypeMatcher withParameterCount(int count) {
        this.matchers.addMatcher(
                description -> description.appendText("has " + count + " parameters"),
                item -> item.parameters().size() == count,
                (item, description) -> description.appendText("had " + item.parameters().size() + " parameters")
        );
        return this;
    }

    public TypeMatcher withParameters(TypeParameterMatcher ... parameterMatchers) {
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
    public boolean matches(Object item) {
        if(Type.class.isInstance(item)) {
            item = TypeInfo.from((Type) item);
        }
        return item != null
                && TypeInfo.class.isInstance(item)
                && matchers.compoundMatcher().matches(item);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (item == null) {
            super.describeMismatch(item, description);
        } else {
            if(Type.class.isInstance(item)) {
                item = TypeInfo.from((Type) item);
            }
            if (! TypeInfo.class.isInstance(item)) {
                description.appendText("was a ")
                        .appendText(item.getClass().getName())
                        .appendText(" (")
                        .appendValue(item)
                        .appendText(")");
            } else {
                this.matchers.compoundMatcher().describeMismatch(item, description);
            }
        }
    }
}
