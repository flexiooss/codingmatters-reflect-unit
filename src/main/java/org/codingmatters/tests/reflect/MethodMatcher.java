package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.MemberDeleguate;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by nelt on 9/8/16.
 */
public class MethodMatcher extends TypeSafeMatcher<Method> {

    static public MethodMatcher aMethod() {
        return new MethodMatcher();
    }

    static public MethodMatcher anInstanceMethod() {
        return new MethodMatcher().notStatic();
    }

    static public MethodMatcher aStaticMethod() {
        return new MethodMatcher().static_();
    }

    private final MatcherChain<Method> matchers = new MatcherChain<>();
    private final MemberDeleguate<MethodMatcher> memberDeleguate;

    private MethodMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }

    public MethodMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }


    public MethodMatcher static_() {
        return this.memberDeleguate.static_(this);
    }

    public MethodMatcher notStatic() {
        return this.memberDeleguate.notStatic(this);
    }

    public MethodMatcher public_() {
        return this.memberDeleguate.public_(this);
    }

    public MethodMatcher private_() {
        return this.memberDeleguate.private_(this);
    }

    public MethodMatcher protected_() {
        return this.memberDeleguate.protected_(this);
    }

    public MethodMatcher packagePrivate() {
        return this.memberDeleguate.packagePrivate(this);
    }

    public MethodMatcher final_() {
        return this.memberDeleguate.final_(this);
    }

    public MethodMatcher abstract_() {
        return this.memberDeleguate.abstract_(this);
    }

    public MethodMatcher withParameters(Class ... parameters) {
        String paramsSpec = Arrays.stream(parameters).map(aClass -> aClass.getName()).collect(Collectors.joining(", "));
        this.matchers.addMatcher("method parameters are " + paramsSpec, item -> Arrays.equals(item.getParameterTypes(), parameters));
        return this;
    }

    public MethodMatcher returning(Class aClass) {
        this.matchers.addMatcher("method returns a " + aClass.getName(), item -> aClass.equals(item.getReturnType()));
        return this;
    }

    public MethodMatcher returningVoid() {
        return this.returning(void.class);
    }


    @Override
    protected boolean matchesSafely(Method aMethod) {
        return matchers.compoundMatcher().matches(aMethod);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Method item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }
}
