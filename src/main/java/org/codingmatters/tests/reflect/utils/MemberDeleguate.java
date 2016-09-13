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
        this.matchers.addMatcher("member named " + name, item -> item.getName().equals(name));
        return self;
    }


    public T static_(T self) {
        this.matchers.addMatcher("instance member", item -> isStatic(item.getModifiers()));
        return self;
    }

    public T notStatic(T self) {
        this.matchers.addMatcher("instance member", item -> ! isStatic(item.getModifiers()));
        return self;
    }

    public T public_(T self) {
        this.matchers.addMatcher("public member", item -> isPublic(item.getModifiers()));
        return self;
    }

    public T private_(T self) {
        this.matchers.addMatcher("private member", item -> isPrivate(item.getModifiers()));
        return self;
    }

    public T protected_(T self) {
        this.matchers.addMatcher("protected member", item -> isProtected(item.getModifiers()));
        return self;
    }

    public T packagePrivate(T self) {
        this.matchers.addMatcher("package private member", item -> ! (isPublic(item.getModifiers()) || isPrivate(item.getModifiers()) || isProtected(item.getModifiers())));
        return self;
    }

    public T abstract_(T self) {
        this.matchers.addMatcher("abstract member", item -> isAbstract(item.getModifiers()));
        return self;
    }

}
