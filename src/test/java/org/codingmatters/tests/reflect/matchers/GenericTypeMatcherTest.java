package org.codingmatters.tests.reflect.matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;
import java.util.List;
import java.util.Spliterator;

import static org.codingmatters.tests.reflect.ReflectMatchers.aGenericType;
import static org.codingmatters.tests.reflect.ReflectMatchers.aMethod;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 10/14/16.
 */
public class GenericTypeMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public class ParametrizedType<T extends Serializable> {}
    public class NotParametrizedType{}

    @Test
    public void parametrizedType() throws Exception {
        assertThat(ParametrizedType.class, is(aGenericType()));
        assertThat(ParametrizedType.class, is(aGenericType().baseClass(ParametrizedType.class)));

        assertThat(ParametrizedType.class, is(TypeMatcher.generic().withParameters(TypeParameterMatcher.typeParameter().named("T").upperBound(TypeMatcher.class_(Serializable.class)))));

        assertThat(ParametrizedType.class, is(aGenericType().baseClass(ParametrizedType.class).withParameters(TypeParameterMatcher.typeParameter().named("T"))));
        assertThat(ParametrizedType.class, is(aGenericType().baseClass(ParametrizedType.class).withParameters(TypeParameterMatcher.typeParameter().named("T").upperBound(TypeMatcher.class_(Serializable.class)))));
    }

    @Test
    public void genericTypeWithVariableType() throws Exception {
        assertThat(Iterable.class, is(aGenericType().withParameters(TypeParameterMatcher.typeParameter().named("T"))));
    }

    @Test
    public void methodWithGenericArrayArgument() throws Exception {
        assertThat(List.class.getMethod("toArray", Object[].class), is(
                aMethod().withParameters(TypeMatcher.typeArray(TypeMatcher.variable().named("T")))
        ));
    }

    @Test
    public void genericReturnType() throws Exception {
        // Spliterator<E> spliterator()
        assertThat(List.class.getMethod("spliterator"), is(
                aMethod().withoutParameters().returning(TypeMatcher.generic().baseClass(Spliterator.class))
        ));
    }

    @Test
    public void notParametrizedType_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (a generic type)\n" +
                "     but: a generic type \"org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$NotParametrizedType\" was not generic"));

        assertThat(NotParametrizedType.class, is(aGenericType()));
    }

    @Test
    public void of_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (a generic type and base class <interface java.io.Serializable>)\n" +
                "     but: base class <interface java.io.Serializable> was <class org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$ParametrizedType>"));

        assertThat(ParametrizedType.class, is(aGenericType().baseClass(Serializable.class)));
    }
}