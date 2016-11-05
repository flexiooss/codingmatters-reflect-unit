package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Created by nelt on 10/13/16.
 */
public class GenericArrayTypeMatcher extends TypeSafeMatcher<Type> {

    private ScrapTypeMatcher typeMatcher;

    public GenericArrayTypeMatcher() {}

    public GenericArrayTypeMatcher of(ScrapTypeMatcher typeMatcher) {
        this.typeMatcher = typeMatcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(Type item) {
        if(this.isArray(item)) {
            if(this.typeMatcher != null) {
                return this.typeMatcher.matchesSafely(((GenericArrayType)item).getGenericComponentType());
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("array");
        if(this.typeMatcher != null) {
            description.appendText(" of ");
            description.appendDescriptionOf(this.typeMatcher);
        }
    }

    @Override
    protected void describeMismatchSafely(Type item, Description mismatchDescription) {
        if(this.isArray(item)) {
            mismatchDescription.appendText("array");
            if(this.typeMatcher != null) {
                mismatchDescription.appendText(" of ");

                this.typeMatcher.describeMismatch(item, mismatchDescription);
            }
        } else {
            mismatchDescription.appendText("not an array");
        }
    }

    private boolean isArray(Type item) {
        return item instanceof GenericArrayType;
    }
}
