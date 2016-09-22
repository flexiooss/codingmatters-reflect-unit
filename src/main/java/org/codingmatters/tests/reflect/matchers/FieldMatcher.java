package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.MemberDeleguate;
import org.codingmatters.tests.reflect.utils.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by nelt on 9/11/16.
 */
public class FieldMatcher extends TypeSafeMatcher<Field> {

    static public FieldMatcher aField(ReflectMatcherConfiguration builder) {
        return new FieldMatcher().configure(builder);
    }

    private final MatcherChain<Field> matchers = new MatcherChain<>();
    private final MemberDeleguate<FieldMatcher> memberDeleguate;

    private FieldMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }


    public FieldMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }

    public FieldMatcher final_() {
        return this.memberDeleguate.final_(this);
    }

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
