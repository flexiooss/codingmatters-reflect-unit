package org.codingmatters.tests.reflect.matchers.impl;

import org.codingmatters.tests.reflect.matchers.TypeMatcher;
import org.codingmatters.tests.reflect.matchers.type.TypeInfo;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Created by nelt on 11/8/16.
 */
public class TypeArrayMatcherImpl extends TypeSafeMatcher<Type> {

    private final TypeMatcher matcher;

    public TypeArrayMatcherImpl(TypeMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("array");
        if (matcher != null) {
            description.appendText(" of ");
            description.appendDescriptionOf(matcher);
        }
    }

    @Override
    protected boolean matchesSafely(Type item) {
        if (Class.class.isInstance(item) && ((Class) item).isArray()) {
            if (matcher == null) return true;
            return matcher.matches(TypeInfo.from(((Class) item).getComponentType()));
        } else if (GenericArrayType.class.isInstance(item)) {
            if (matcher == null) return true;
            return matcher.matches(TypeInfo.from(((GenericArrayType) item).getGenericComponentType()));
        } else {
            return false;
        }
    }

    @Override
    protected void describeMismatchSafely(Type item, Description mismatchDescription) {
        Type it;
        if (Class.class.isInstance(item) && ((Class) item).isArray()) {
            mismatchDescription.appendText("array");
            it = ((Class) item).getComponentType();
        } else if (GenericArrayType.class.isInstance(item)) {
            mismatchDescription.appendText("array");
            it = ((GenericArrayType) item).getGenericComponentType();
        } else {
            mismatchDescription.appendText("not an array");
            return;
        }


        if (matcher != null) {
            mismatchDescription.appendText(" of ");

            matcher.describeMismatch(it, mismatchDescription);
        }
    }
}
