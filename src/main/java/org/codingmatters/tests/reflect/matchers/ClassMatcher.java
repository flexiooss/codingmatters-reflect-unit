package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.utils.LambdaMatcher;
import org.codingmatters.tests.reflect.utils.LevelModifier;
import org.codingmatters.tests.reflect.utils.MatcherChain;
import org.codingmatters.tests.reflect.utils.ReflectMatcherConfiguration;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.reflect.Modifier.*;

/**
 * Created by nelt on 9/6/16.
 */
public class ClassMatcher extends TypeSafeMatcher<Class> {

    public static ClassMatcher anInterface(ReflectMatcherConfiguration builder) {
        return new ClassMatcher()
                .addMatcher("interface", item -> isInterface(item.getModifiers()))
                .configure(builder);
    }

    public static ClassMatcher aClass(ReflectMatcherConfiguration builder) {
        return new ClassMatcher()
                .addMatcher("class", item -> !isInterface(item.getModifiers()))
                .configure(builder);
    }


    private final MatcherChain<Class> matchers = new MatcherChain<>();

    private ClassMatcher() {}

    public ClassMatcher named(String name) {
        this.addMatcher(
                "named " + name,
                item -> item.getName().equals(name),
                (item, description) -> description.appendText("was " + item.getName())
        );
        return this;
    }

    public ClassMatcher with(MethodMatcher methodMatcher) {
        this.matchers.add(new CollectorMatcher<Method, Class>(methodMatcher, item -> {
            List<Method> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredMethods()));
            return result;
        }));
        return this;
    }

    public ClassMatcher with(FieldMatcher fieldMatcher) {
        this.matchers.add(new CollectorMatcher<Field, Class>(fieldMatcher, item -> {
            List<Field> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredFields()));
            return result;
        }));
        return this;
    }

    public ClassMatcher withParameter(TypeMatcher typeMatcher) {
        this.matchers.add(new CollectorMatcher<Type, Class>(typeMatcher, item -> {
            List<Type> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getTypeParameters()));
            return result;
        }));
        return this;
    }



    public ClassMatcher with(ConstructorMatcher constructorMatcher) {
        this.matchers.add(new CollectorMatcher<Constructor, Class>(constructorMatcher, item -> {
            List<Constructor> result = new LinkedList<>();
            result.addAll(Arrays.asList(item.getDeclaredConstructors()));
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
                this.addMatcher(
                        "public",
                        item -> isPublic(item.getModifiers()),
                        this.accessModifierMismatch());
                break;
            case PRIVATE:
                this.addMatcher(
                        "private",
                        item -> isPrivate(item.getModifiers()),
                        this.accessModifierMismatch());
                break;
            case PROTECTED:
                this.addMatcher(
                        "protected",
                        item -> isProtected(item.getModifiers()),
                        this.accessModifierMismatch());
                break;
            case PACKAGE_PRIVATE:
                this.addMatcher("" +
                        "package private",
                        item -> ! (isPublic(item.getModifiers()) || isPrivate(item.getModifiers()) || isProtected(item.getModifiers())),
                        this.accessModifierMismatch());
                break;
        }
        return this;
    }

    private LambdaMatcher.ItemDescripitor<Class> accessModifierMismatch() {
        return (item, description) -> {
            if(isPublic(item.getModifiers())) {
                description.appendText("was public");
            } else if(isPrivate(item.getModifiers())) {
                description.appendText("was private");
            } else if(isProtected(item.getModifiers())) {
                description.appendText("was protected");
            } else {
                description.appendText("was package private");
            }
        };
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

    private ClassMatcher addMatcher(String description, LambdaMatcher.Matcher<Class> lambda) {
        return this.addMatcher(description, lambda, null);
    }

    private ClassMatcher addMatcher(String description, LambdaMatcher.Matcher<Class> lambda, LambdaMatcher.ItemDescripitor<Class> mismatchDescripitor) {
        this.matchers.add(LambdaMatcher.match(description, lambda, mismatchDescripitor));
        return this;
    }

    public ClassMatcher implementing(Class interfaceClass) {
        this.matchers.addMatcher(
                "implements " + interfaceClass.getName(),
                item -> Arrays.asList(item.getInterfaces()).contains(interfaceClass),
                (item, description) -> description.appendText("was false (" + item.getName() + " implements " + Arrays.asList(item.getInterfaces()) + ")")
        );
        return this;
    }

    public ClassMatcher extending(Class aClass) {
        this.matchers.addMatcher(
                "extends " + aClass.getName(),
                item -> item.getSuperclass().equals(aClass),
                (item, description) -> description.appendText("was false")
        );
        return this;
    }
}
