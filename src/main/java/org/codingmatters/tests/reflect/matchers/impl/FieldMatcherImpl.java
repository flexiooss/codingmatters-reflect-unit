package org.codingmatters.tests.reflect.matchers.impl;

import org.codingmatters.tests.reflect.matchers.FieldMatcher;
import org.codingmatters.tests.reflect.matchers.support.MatcherChain;
import org.codingmatters.tests.reflect.matchers.support.MemberDeleguate;
import org.codingmatters.tests.reflect.matchers.support.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by nelt on 9/11/16.
 */
public class FieldMatcherImpl extends TypeSafeMatcher<Field> implements FieldMatcher {

    static public FieldMatcher aField(ReflectMatcherConfiguration builder) {
        return new FieldMatcherImpl().configure(builder);
    }

    private final MatcherChain<Field> matchers = new MatcherChain<>();
    private final MemberDeleguate<FieldMatcher> memberDeleguate;

    private FieldMatcherImpl() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }


    @Override
    public FieldMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }

    @Override
    public FieldMatcher final_() {
        return this.memberDeleguate.final_(this);
    }

    @Override
    public FieldMatcher withType(Class type) {
        this.matchers.addMatcher("field type", item -> item.getType().equals(type));
        return this;
    }

    @Override
    protected boolean matchesSafely(Field aField) {
        return matchers.compoundMatcher().matches(aField);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("filed");
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Field item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    private FieldMatcher configure(ReflectMatcherConfiguration builder) {
        builder.levelModifier().apply(this.memberDeleguate, this);
        builder.accessModifier().apply(this.memberDeleguate, this);
        return this;
    }
}
