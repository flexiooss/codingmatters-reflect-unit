package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.MemberDeleguate;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created by nelt on 9/11/16.
 */
public class FieldMatcher extends TypeSafeMatcher<Field> {

    static public FieldMatcher aField() {
        return new FieldMatcher();
    }

    static public FieldMatcher aStaticField() {
        return new FieldMatcher().static_();
    }

    static public FieldMatcher anInstanceField() {
        return new FieldMatcher().notStatic();
    }

    private final MatcherChain<Field> matchers = new MatcherChain<>();
    private final MemberDeleguate<FieldMatcher> memberDeleguate;

    public FieldMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }


    public FieldMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }

    public FieldMatcher static_() {
        return this.memberDeleguate.static_(this);
    }

    public FieldMatcher notStatic() {
        return this.memberDeleguate.notStatic(this);
    }

    public FieldMatcher public_() {
        return this.memberDeleguate.public_(this);
    }

    public FieldMatcher private_() {
        return this.memberDeleguate.private_(this);
    }

    public FieldMatcher protected_() {
        return this.memberDeleguate.protected_(this);
    }

    public FieldMatcher packagePrivate() {
        return this.memberDeleguate.packagePrivate(this);
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
}
