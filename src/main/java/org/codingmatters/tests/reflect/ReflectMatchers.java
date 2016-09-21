package org.codingmatters.tests.reflect;

/**
 * Created by nelt on 9/8/16.
 */
public class ReflectMatchers {

    static public ConstructorMatcher aConstructor() {
        return new ReflectMatcherBuilder().constructor();
    }

    static public ReflectMatcherBuilder aStatic() {
        return new ReflectMatcherBuilder().static_();
    }

    static public ReflectMatcherBuilder anInstance() {
        return new ReflectMatcherBuilder().instance_();
    }

    static public ReflectMatcherBuilder aPublic() {
        return new ReflectMatcherBuilder().public_();
    }

    static public ReflectMatcherBuilder aPrivate() {
        return new ReflectMatcherBuilder().private_();
    }

    static public ReflectMatcherBuilder aProtected() {
        return new ReflectMatcherBuilder().protected_();
    }

    static public ReflectMatcherBuilder aPackagePrivate() {
        return new ReflectMatcherBuilder().packagePrivate();
    }

}
