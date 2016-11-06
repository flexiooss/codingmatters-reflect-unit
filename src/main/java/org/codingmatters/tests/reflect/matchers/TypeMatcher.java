package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.internal.TypeInfo;
import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeMatcher extends BaseMatcher<Type> {


    public static Matcher<Type> typeArray() {
        return typeArray(null);
    }

    public static Matcher<Type> typeArray(TypeMatcher matcher) {
        return new TypeSafeMatcher<Type>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("array");
                if(matcher != null) {
                    description.appendText(" of ");
                    description.appendDescriptionOf(matcher);
                }
            }

            @Override
            protected boolean matchesSafely(Type item) {
                if(Class.class.isInstance(item) && ((Class)item).isArray()) {
                    if(matcher == null) return true;
                    return matcher.matches(TypeInfo.from(((Class)item).getComponentType()));
                } else if(GenericArrayType.class.isInstance(item)){
                    if(matcher == null) return true;
                    return matcher.matches(TypeInfo.from(((GenericArrayType)item).getGenericComponentType()));
                } else {
                    return false;
                }
            }

            @Override
            protected void describeMismatchSafely(Type item, Description mismatchDescription) {
                Type it;
                if(Class.class.isInstance(item) && ((Class)item).isArray()) {
                    mismatchDescription.appendText("array");
                    it = ((Class)item).getComponentType();
                } else if(GenericArrayType.class.isInstance(item)){
                    mismatchDescription.appendText("array");
                    it = ((GenericArrayType)item).getGenericComponentType();
                } else {
                    mismatchDescription.appendText("not an array");
                    return;
                }


                if(matcher != null) {
                    mismatchDescription.appendText(" of ");

                    matcher.describeMismatch(it, mismatchDescription);
                }
            }
        };
    }

    public static TypeMatcher generic() {
        return new TypeMatcher().generic_();
    }

    public static TypeMatcher nonGeneric() {
        return new TypeMatcher().nonGeneric_();
    }

    public static TypeMatcher variable() {
        return new TypeMatcher().variable_();
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

    private TypeMatcher variable_() {
        this.matchers.addMatcher(
                description -> description.appendText("a variable type"),
                item -> item.isVariable(),
                (item, description) -> description.appendValue(item.name()).appendText(" was not a variable")
        );
        return this;
    }

    public TypeMatcher named(String name) {
        this.matchers.addMatcher(
                description -> description.appendText("named ").appendValue(name),
                item -> name.equals(item.name()),
                (item, description) -> description.appendText("name was ").appendValue(item.name())
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
