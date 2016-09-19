package org.codingmatters.tests.reflect;

/**
 * Created by nelt on 9/8/16.
 */
public class ReflectMatchers {

    static public ConstructorMatcher aConstructor() {
        return ConstructorMatcher.aConstructor();
    }

    static public StaticMatcher aStatic_() {
        return new StaticMatcher();
    }

    static public InstanceMatcher anInstance() {
        return new InstanceMatcher();
    }

}
