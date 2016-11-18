package org.codingmatters.tests.reflect.matchers.impl;

import org.codingmatters.tests.reflect.matchers.ConstructorMatcher;
import org.codingmatters.tests.reflect.matchers.support.MatcherChain;
import org.codingmatters.tests.reflect.matchers.support.MemberDeleguate;
import org.codingmatters.tests.reflect.matchers.support.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by nelt on 9/15/16.
 */
public class ConstructorMatcherImpl extends TypeSafeMatcher<Constructor> implements ConstructorMatcher {

    static public ConstructorMatcher aConstructor(ReflectMatcherConfiguration configuration) {
        return new ConstructorMatcherImpl().configure(configuration);
    }

    private final MatcherChain<Constructor> matchers = new MatcherChain<>();
    private final MemberDeleguate<ConstructorMatcher> memberDeleguate;

    private ConstructorMatcherImpl() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }

    @Override
    public ConstructorMatcher withParameters(Class... parameters) {
        String paramsSpec = Arrays.stream(parameters).map(aClass -> aClass.getName()).collect(Collectors.joining(", "));
        this.matchers.addMatcher("method parameters are " + paramsSpec, item -> Arrays.equals(item.getParameterTypes(), parameters));
        return this;
    }

    @Override
    public ConstructorMatcher withParameters(Matcher<Type>... typeMatcher) {
        if(typeMatcher == null) {
            this.matchers.addMatcher("no parameters",
                    item -> item.getParameterCount() == 0,
                    (item, description) -> description.appendText("was " + item.getParameterCount())
            );
        } else {
            this.matchers.addMatcher(typeMatcher.length + " parameters",
                    item -> item.getParameterCount() == typeMatcher.length,
                    (item, description) -> description.appendText("was " + item.getParameterCount())
            );

            for (int i = 0; i < typeMatcher.length; i++) {
                int index = i;
                this.matchers.addMatcher(
                        description -> description
                                .appendText("parameter[" + index + "]" + " is ")
                                .appendDescriptionOf(typeMatcher[index]),
                        item -> typeMatcher[index].matches(item.getGenericParameterTypes()[index]),
                        (item, description) -> typeMatcher[index].describeMismatch(item, description)
                );
            }
        }
        return this;
    }

    @Override
    protected boolean matchesSafely(Constructor constructor) {
        return matchers.compoundMatcher().matches(constructor);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("constructor");
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Constructor item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    private ConstructorMatcher configure(ReflectMatcherConfiguration configuration) {
        configuration.accessModifier().apply(this.memberDeleguate, this);
        return this;
    }
}
