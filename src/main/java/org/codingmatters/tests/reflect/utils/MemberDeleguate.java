package org.codingmatters.tests.reflect.utils;

import java.lang.reflect.Member;

import static java.lang.reflect.Modifier.*;

/**
 * Created by nelt on 9/11/16.
 */
public class MemberDeleguate<T> {

    private final MatcherChain<Member> matchers;

    public MemberDeleguate(MatcherChain matchers) {
        this.matchers = matchers;
    }


    public T named(String name, T self) {
        this.matchers.addMatcher(
                "named " + name,
                item -> item.getName().equals(name),
                item -> item.getName()
        );
        return self;
    }


    public T static_(T self) {
        this.matchers.addMatcher(
                "static",
                item -> isStatic(item.getModifiers()),
                item -> "not static");
        return self;
    }

    public T instance(T self) {
        this.matchers.addMatcher(
                "instance",
                item -> ! isStatic(item.getModifiers()),
                item -> "static");
        return self;
    }

    public T public_(T self) {
        this.matchers.addMatcher(
                "public",
                item -> isPublic(item.getModifiers()),
                this.accessModifierMismatch());
        return self;
    }

    public T private_(T self) {
        this.matchers.addMatcher(
                "private",
                item -> isPrivate(item.getModifiers()),
                this.accessModifierMismatch());
        return self;
    }

    public T protected_(T self) {
        this.matchers.addMatcher(
                "protected",
                item -> isProtected(item.getModifiers()),
                this.accessModifierMismatch());
        return self;
    }

    public T packagePrivate(T self) {
        this.matchers.addMatcher(
                "package private",
                item -> ! (isPublic(item.getModifiers()) || isPrivate(item.getModifiers()) || isProtected(item.getModifiers())),
                this.accessModifierMismatch());
        return self;
    }

    public T final_(T self) {
        this.matchers.addMatcher(
                "final",
                item -> isFinal(item.getModifiers()),
                item -> "not final");
        return self;
    }

    public T abstract_(T self) {
        this.matchers.addMatcher(
                "abstract",
                item -> isAbstract(item.getModifiers()),
                item -> "concrete");
        return self;
    }


    private LambdaMatcher.MismatchDescripitor<Member> accessModifierMismatch() {
        return item -> {
            if(isPublic(item.getModifiers())) {
                return "was public";
            } else if(isPrivate(item.getModifiers())) {
                return "was private";
            } else if(isProtected(item.getModifiers())) {
                return "was protected";
            } else {
                return "was package private";
            }
        };
    }
}
