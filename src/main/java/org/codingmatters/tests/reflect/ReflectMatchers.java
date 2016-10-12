package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.matchers.*;

/**
 * Created by nelt on 9/8/16.
 */
public class ReflectMatchers {

    static public ReflectMatcherBuilder aStatic() {
        return new ReflectMatcherBuilder().static_();
    }

    static public ReflectMatcherBuilder anInstance() {
        return new ReflectMatcherBuilder().instance();
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

    static public ConstructorMatcher aConstructor() {
        return new ReflectMatcherBuilder().constructor();
    }

    static public ClassMatcher aClass() {
        return anInstance().class_();
    }

    public static ClassMatcher anInterface() {
        return anInstance().interface_();
    }

    public static FieldMatcher aField() {
        return anInstance().field();
    }

    public static MethodMatcher aMethod() {
        return anInstance().method();
    }

    public static TypeVariableMatcher aTypeVariable() {
        return new TypeVariableMatcher();
    }

}
