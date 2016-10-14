package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.MemberDeleguate;
import org.codingmatters.tests.reflect.utils.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.annotation.Annotation;
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

    public MethodMatcher withParameters(TypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Method>(
                typeMatcher,
                item -> Arrays.asList(item.getGenericParameterTypes())));
        return this;
    }

    public MethodMatcher withParameters(GenericArrayTypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Method>(
                typeMatcher,
                item -> Arrays.asList(item.getGenericParameterTypes())));
        return this;
    }

    public MethodMatcher returning(Class aClass) {
        this.matchers.addMatcher("method returns a " + aClass.getName(), item -> aClass.equals(item.getReturnType()));
        return this;
    }

    public MethodMatcher returningVoid() {
        return this.returning(void.class);
    }

    public MethodMatcher returning(TypeMatcher typeMatcher) {
        this.matchers.add(new MethodElementTypeMatcher(typeMatcher, method -> method.getGenericReturnType()));
        return this;
    }

    public MethodMatcher returning(GenericArrayTypeMatcher genericArrayTypeMatcher) {
        this.matchers.add(new MethodElementTypeMatcher(genericArrayTypeMatcher, method -> method.getGenericReturnType()));
        return this;
    }

    public MethodMatcher returning(GenericTypeMatcher typeMatcher) {
        this.matchers.add(new MethodElementTypeMatcher(typeMatcher, method -> method.getGenericReturnType()));
        return this;
    }

    public MethodMatcher with(TypeMatcher typeMatcher) {
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
        this.matchers.addMatcher("method is anotated with " + anotationClass.getName(), item -> {
            for (Annotation annotation : item.getAnnotations()) {
                System.out.println(annotation);
            }

            return item.getAnnotation(anotationClass) != null;
        });
        return this;
    }

    private static class MethodElementTypeMatcher extends TypeSafeMatcher<Method> {
        private final TypeSafeMatcher typeMatcher;
        private final MethodElementTypeSupplier typeSupplier;

        public MethodElementTypeMatcher(TypeSafeMatcher typeMatcher, MethodElementTypeSupplier typeSupplier) {
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
