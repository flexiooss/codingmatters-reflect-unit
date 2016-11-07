package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.support.CollectorMatcher;
import org.codingmatters.tests.reflect.matchers.support.MatcherChain;
import org.codingmatters.tests.reflect.matchers.support.MemberDeleguate;
import org.codingmatters.tests.reflect.matchers.support.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nelt on 9/8/16.
 */
public class MethodMatcher extends TypeSafeMatcher<Method> {

    static public MethodMatcher aMethod(ReflectMatcherConfiguration builder) {
        return new MethodMatcher().configure(builder);
    }

    private final MatcherChain<Method> matchers = new MatcherChain<>();
    private final MemberDeleguate<MethodMatcher> memberDeleguate;

    private MethodMatcher() {
        this.memberDeleguate = new MemberDeleguate<>(this.matchers);
    }

    public MethodMatcher named(String name) {
        return this.memberDeleguate.named(name, this);
    }

    public MethodMatcher final_() {
        return this.memberDeleguate.final_(this);
    }

    public MethodMatcher abstract_() {
        return this.memberDeleguate.abstract_(this);
    }

    public MethodMatcher withParameters(Class ... parameters) {
        String paramsSpec = Arrays.stream(parameters).map(aClass -> aClass.getName()).collect(Collectors.joining(", "));
        this.matchers.addMatcher(
                "method parameters are " + paramsSpec,
                item -> Arrays.equals(item.getParameterTypes(), parameters));
        return this;
    }

    public MethodMatcher withParameters(Matcher<Type>... typeMatcher) {
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

    public MethodMatcher returning(Class aClass) {
        return this.returning(TypeMatcher.class_(aClass));
    }

    public MethodMatcher returning(Matcher<Type> typeMatcher) {
        this.matchers.add(new MethodElementTypeMatcher(typeMatcher, method -> method.getGenericReturnType()));
        return this;
    }

    public MethodMatcher returningVoid() {
        return this.returning(void.class);
    }

    public MethodMatcher withVariable(TypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Method>(typeMatcher, item -> {
            List<Type> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getTypeParameters()));
            return result;
        }));
        return this;
    }

    @Override
    protected boolean matchesSafely(Method aMethod) {
        return matchers.compoundMatcher().matches(aMethod);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("method");
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Method item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    private MethodMatcher configure(ReflectMatcherConfiguration builder) {
        builder.levelModifier().apply(this.memberDeleguate, this);
        builder.accessModifier().apply(this.memberDeleguate, this);
        return this;
    }

    /**
     * Tests if a RUNTIME retention level annotation decorates the method.
     * @param anotationClass
     * @return
     */
    public MethodMatcher anotatedWith(Class anotationClass) {
        this.matchers.addMatcher("method is anotated with " + anotationClass.getName(), item -> item.getAnnotation(anotationClass) != null);
        return this;
    }

    public MethodMatcher withoutParameters() {
        this.matchers.addMatcher(
                "no parameters",
                item -> item.getParameterCount() == 0,
                (item, description) -> description.appendText("was " + item.getParameterCount())
        );
        return this;
    }

    private static class MethodElementTypeMatcher extends TypeSafeMatcher<Method> {
        private final Matcher<Type> typeMatcher;
        private final MethodElementTypeSupplier typeSupplier;

        public MethodElementTypeMatcher(Matcher<Type> typeMatcher, MethodElementTypeSupplier typeSupplier) {
            this.typeMatcher = typeMatcher;
            this.typeSupplier = typeSupplier;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("return (");
            typeMatcher.describeTo(description);
            description.appendText(")");
        }

        @Override
        protected boolean matchesSafely(Method item) {
            Type type = this.typeSupplier.getFrom(item);
            if(type != null) {
                return typeMatcher.matches(type);
            } else {
                return false;
            }
        }

        @Override
        protected void describeMismatchSafely(Method item, Description mismatchDescription) {
            mismatchDescription.appendText("was (");
            typeMatcher.describeMismatch(this.typeSupplier.getFrom(item), mismatchDescription);
            mismatchDescription.appendText(")");
        }

        @FunctionalInterface
        interface MethodElementTypeSupplier {
            Type getFrom(Method method);
        }
    }
}
