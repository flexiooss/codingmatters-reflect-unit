package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.MemberDeleguate;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by nelt on 9/15/16.
 */
public class ConstructorMatcher extends TypeSafeMatcher<Constructor> {
    static public ConstructorMatcher aConstructor() {
        return new ConstructorMatcher();
    }

    private final MatcherChain<Constructor> matchers = new MatcherChain<>();
    private final MemberDeleguate<ConstructorMatcher> memberDeleguate;

    private ConstructorMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }

    public ConstructorMatcher public_() {
        return this.memberDeleguate.public_(this);
    }

    public ConstructorMatcher private_() {
        return this.memberDeleguate.private_(this);
    }

    public ConstructorMatcher protected_() {
        return this.memberDeleguate.protected_(this);
    }

    public ConstructorMatcher packagePrivate() {
        return this.memberDeleguate.packagePrivate(this);
    }

    public ConstructorMatcher abstract_() {
        return this.memberDeleguate.abstract_(this);
    }

    public ConstructorMatcher withParameters(Class ... parameters) {
        String paramsSpec = Arrays.stream(parameters).map(aClass -> aClass.getName()).collect(Collectors.joining(", "));
        this.matchers.addMatcher("method parameters are " + paramsSpec, item -> Arrays.equals(item.getParameterTypes(), parameters));
        return this;
    }

    @Override
    protected boolean matchesSafely(Constructor constructor) {
        return matchers.compoundMatcher().matches(constructor);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Constructor item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }
}
