package org.codingmatters.tests.reflect.matchers.impl;

import org.codingmatters.tests.reflect.matchers.TypeMatcher;
import org.codingmatters.tests.reflect.matchers.TypeParameterMatcher;
import org.codingmatters.tests.reflect.matchers.support.MatcherChain;
import org.codingmatters.tests.reflect.matchers.type.TypeInfo;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.lang.reflect.Type;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeMatcherImpl extends BaseMatcher<Type> implements TypeMatcher {

    private final MatcherChain<TypeInfo> matchers = new MatcherChain<>();

    public TypeMatcherImpl() {
    }

    public TypeMatcher generic_() {
        this.matchers.addMatcher(
                description -> description.appendText("a generic type"),
                item -> item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not generic"));
        return this;
    }

    public TypeMatcher nonGeneric_() {
        this.matchers.addMatcher(
                description -> description.appendText("a non generic type"),
                item -> ! item.isGeneric(),
                (item, description) -> description.appendValue(item.name()).appendText(" was generic"));
        return this;
    }

    @Override
    public TypeMatcherImpl baseClass(Class aClass) {
        this.matchers.addMatcher(
                description -> description.appendText("base class ").appendValue(aClass),
                item -> aClass.equals(item.baseClass()),
                (item, description) -> description.appendText("was ").appendValue(item.baseClass())
        );
        return this;
    }

    public TypeMatcher variable_() {
        this.matchers.addMatcher(
                description -> description.appendText("a variable type"),
                item -> item.isVariable(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not a variable")
        );
        return this;
    }

    @Override
    public TypeMatcher named(String name) {
        this.matchers.addMatcher(
                description -> description.appendText("named ").appendValue(name),
                item -> name.equals(item.name()),
                (item, description) -> description.appendText("name was ").appendValue(item.name())
        );
        return this;
    }


    @Override
    public TypeMatcher withParameterCount(int count) {
        this.matchers.addMatcher(
                description -> description.appendText("has " + count + " parameters"),
                item -> item.parameters().size() == count,
                (item, description) -> description.appendText("had " + item.parameters().size() + " parameters")
        );
        return this;
    }

    @Override
    public TypeMatcher withParameters(TypeParameterMatcher... parameterMatchers) {
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
