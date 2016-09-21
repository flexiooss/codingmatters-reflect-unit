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

    static public MethodMatcher anInstanceMethod() {
        return new MethodMatcher().instance();
    }

    static public MethodMatcher aStaticMethod() {
        return new MethodMatcher().static_();
    }

    public static MethodMatcher method(ReflectMatcherConfiguration builder) {
        return new MethodMatcher().configure(builder);
    }

    private MethodMatcher configure(ReflectMatcherConfiguration builder) {
        if(builder.levelModifier().equals(LevelModifier.INSTANCE)) {
            this.instance();
        } else {
            this.static_();
        }

        switch (builder.accessModifier()) {
            case PUBLIC:
                this.public_();
                break;
            case PRIVATE:
                this.private_();
                break;
            case PROTECTED:
                this.protected_();
                break;
            case PACKAGE_PRIVATE:
                this.packagePrivate();
                break;
        }
        return this;
    }

    private final MatcherChain<Method> matchers = new MatcherChain<>();
    private final MemberDeleguate<MethodMatcher> memberDeleguate;

    private MethodMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }

    public MethodMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }


    private MethodMatcher static_() {
        return this.memberDeleguate.static_(this);
    }

    private MethodMatcher instance() {
        return this.memberDeleguate.notStatic(this);
    }

    private MethodMatcher public_() {
        return this.memberDeleguate.public_(this);
    }

    private MethodMatcher private_() {
        return this.memberDeleguate.private_(this);
    }

    private MethodMatcher protected_() {
        return this.memberDeleguate.protected_(this);
    }

    private MethodMatcher packagePrivate() {
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
