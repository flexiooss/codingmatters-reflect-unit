package org.codingmatters.tests.reflect.matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;
import java.util.List;
import java.util.Spliterator;

import static org.codingmatters.tests.reflect.ReflectMatchers.*;
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
        assertThat(ParametrizedType.class, is(aGenericType().of(ParametrizedType.class)));
        assertThat(ParametrizedType.class, is(aGenericType().with(aVariableType().named("T").withBound(Serializable.class))));
        assertThat(ParametrizedType.class, is(aGenericType().of(ParametrizedType.class).with(aVariableType().named("T"))));
        assertThat(ParametrizedType.class, is(aGenericType().of(ParametrizedType.class).with(aVariableType().named("T").withBound(Serializable.class))));
    }

    @Test
    public void genericTypeWithVariableType() throws Exception {
        assertThat(Iterable.class, is(aGenericType().with(aVariableType().named("T"))));
    }

    @Test
    public void methodWithGenericArrayArgument() throws Exception {
        assertThat(List.class.getMethod("toArray", Object[].class), is(
                aMethod().withParameters(aGenericArray().of(aVariableType().named("T")))
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
                "Expected: is (generic)\n" +
                "     but: generic org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$NotParametrizedType is not generic"));

        assertThat(NotParametrizedType.class, is(new GenericTypeMatcher()));
    }

    @Test
    public void of_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (generic and of java.io.Serializable)\n" +
                "     but: of java.io.Serializable was of org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$ParametrizedType"));

        assertThat(ParametrizedType.class, is(aGenericType().of(Serializable.class)));
    }
}