package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.utils.LambdaMatcher;
import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.reflect.Modifier.*;

/**
 * Created by nelt on 9/6/16.
 */
public class ClassMatcher extends TypeSafeMatcher<Class> {

    static ClassMatcher anInterface(ReflectMatcherConfiguration builder) {
        return new ClassMatcher()
                .addMatcher("interface", item -> Modifier.isInterface(item.getModifiers()))
                .configure(builder);
    }

    static ClassMatcher aClass(ReflectMatcherConfiguration builder) {
        return new ClassMatcher()
                .addMatcher("class", item -> !Modifier.isInterface(item.getModifiers()))
                .configure(builder);
    }


    private final MatcherChain<Class> matchers = new MatcherChain<>();

    private ClassMatcher() {}

    public ClassMatcher named(String name) {
        this.addMatcher("class named " + name, item -> item.getName().equals(name));
        return this;
    }

    public ClassMatcher with(MethodMatcher methodMatcher) {
        this.matchers.add(new ClassMemberMatcher<>(methodMatcher, item -> {
            List<Method> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredMethods()));
            result.addAll(Arrays.asList(item.getMethods()));
            return result;
        }));
        return this;
    }

    public Matcher<Class> with(FieldMatcher fieldMatcher) {
        this.matchers.add(new ClassMemberMatcher<>(fieldMatcher, item -> {
            List<Field> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredFields()));
            result.addAll(Arrays.asList(item.getFields()));
            return result;
        }));
        return this;
    }



    public ClassMatcher with(ConstructorMatcher constructorMatcher) {
        this.matchers.add(new ClassMemberMatcher<>(constructorMatcher, item -> {
            List<Constructor> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredConstructors()));
            result.addAll(Arrays.asList(item.getConstructors()));
            return result;
        }));
        return this;
    }


    private ClassMatcher static_() {
        return this.addMatcher("static", item -> isStatic(item.getModifiers()));
    }

    private ClassMatcher instance_() {
        return this.addMatcher("instance", item -> ! isStatic(item.getModifiers()));
    }

    private ClassMatcher configure(ReflectMatcherConfiguration builder) {
        if(builder.levelModifier().equals(LevelModifier.INSTANCE)) {
            this.instance_();
        } else {
            this.static_();
        }

        switch (builder.accessModifier()) {
            case PUBLIC:
                this.addMatcher("public", item -> isPublic(item.getModifiers()));
                break;
            case PRIVATE:
                this.addMatcher("private", item -> isPrivate(item.getModifiers()));
                break;
            case PROTECTED:
                this.addMatcher("protected", item -> isProtected(item.getModifiers()));
                break;
            case PACKAGE_PRIVATE:
                this.addMatcher("package private", item -> ! (isPublic(item.getModifiers()) || isPrivate(item.getModifiers()) || isProtected(item.getModifiers())));
                break;
        }
        return this;
    }


    public ClassMatcher final_() {
        return this.addMatcher("final", item -> isFinal(item.getModifiers()));
    }



    @Override
    protected boolean matchesSafely(Class aClass) {
        return this.matchers.compoundMatcher().matches(aClass);
    }

    @Override
    public void describeTo(Description description) {
        this.matchers.compoundMatcher().describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Class item, Description mismatchDescription) {
        this.matchers.compoundMatcher().describeMismatch(item, mismatchDescription);
    }

    private ClassMatcher addMatcher(String description, LambdaMatcher.Lambda<Class> lambda) {
        this.matchers.add(LambdaMatcher.match(description, lambda));
        return this;
    }

    public ClassMatcher implementing(Class interfaceClass) {
        this.matchers.addMatcher("implements an interface", item -> isInterface(interfaceClass.getModifiers()));
        this.matchers.addMatcher("implements " + interfaceClass.getName(), item -> Arrays.asList(item.getInterfaces()).contains(interfaceClass));
        return this;
    }

    public ClassMatcher extending(Class aClass) {
        this.matchers.addMatcher("extends " + aClass.getName(), item -> item.getSuperclass().equals(aClass));
        return this;
    }

    static private class ClassMemberMatcher<T extends Member> extends TypeSafeMatcher<Class> {

        private final TypeSafeMatcher<T> methodMatcher;
        private final MemberCollector<T> memberCollector;

        private ClassMemberMatcher(TypeSafeMatcher<T> methodMatcher, MemberCollector<T> memberCollector) {
            this.methodMatcher = methodMatcher;
            this.memberCollector = memberCollector;
        }

        @Override
        protected boolean matchesSafely(Class item) {
            for (T method : this.memberCollector.candidates(item)) {
                if(this.methodMatcher.matches(method)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {

        }

        private interface MemberCollector<T extends Member> {
            List<T> candidates(Class item);
        }
    }

}
