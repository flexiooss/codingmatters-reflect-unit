package org.codingmatters.tests.reflect.matchers;

import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by nelt on 11/8/16.
 */
public interface MethodMatcher extends Matcher<Method> {
    MethodMatcher named(String name);

    MethodMatcher final_();

    MethodMatcher abstract_();

    MethodMatcher withParameters(Class... parameters);

    MethodMatcher withParameters(Matcher<Type>... typeMatcher);

    MethodMatcher returning(Class aClass);

    MethodMatcher returning(Matcher<Type> typeMatcher);

    MethodMatcher returningVoid();

    MethodMatcher withVariable(TypeMatcher typeMatcher);

    MethodMatcher withoutParameters();

    MethodMatcher throwing(Class ... exceptionClasses);
    MethodMatcher throwing(Matcher<Type> ... exceptionMatchers);
    MethodMatcher notThrowing();
}
