package org.codingmatters.tests.reflect;

import org.codingmatters.tests.reflect.matchers.*;
import org.codingmatters.tests.reflect.matchers.impl.TypeArrayMatcherImpl;
import org.codingmatters.tests.reflect.matchers.impl.TypeMatcherImpl;
import org.codingmatters.tests.reflect.matchers.impl.TypeParameterMatcherImpl;
import org.hamcrest.Matcher;

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
        return new ReflectMatcherBuilder().public_().constructor();
    }

    static public ClassMatcher aClass() {
        return new ReflectMatcherBuilder().public_().instance().class_();
    }

    public static ClassMatcher anInterface() {
        return new ReflectMatcherBuilder().public_().instance().interface_();
    }

    public static FieldMatcher aField() {
        return new ReflectMatcherBuilder().public_().instance().field();
    }

    public static MethodMatcher aMethod() {
        return new ReflectMatcherBuilder().public_().instance().method();
    }

    public static TypeParameterMatcher typeParameter() {
        return TypeParameterMatcherImpl.typeParameter();
    }

    public static Matcher<java.lang.reflect.Type> typeArray() {
        return new TypeArrayMatcherImpl(null);
    }

    public static Matcher<java.lang.reflect.Type> typeArray(TypeMatcher matcher) {
        return new TypeArrayMatcherImpl(matcher);
    }

    public static TypeMatcher genericType() {
        return new TypeMatcherImpl().generic_();
    }

    public static TypeMatcher nonGenericType() {
        return new TypeMatcherImpl().nonGeneric_();
    }

    public static TypeMatcher variableType() {
        return new TypeMatcherImpl().variable_();
    }

    public static TypeMatcherImpl classType(Class aClass) {
        return new TypeMatcherImpl().baseClass(aClass);
    }
}
