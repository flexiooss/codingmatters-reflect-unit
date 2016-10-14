package org.codingmatters.tests.reflect.matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.TypeVariable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 10/12/16.
 */
public class TypeMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public class ParametrizedType<T extends Serializable> {}

    @Test
    public void name() throws Exception {
        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().named("T")));
    }

    @Test
    public void name_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is type variable(named NotNamedLikeThat)\n" +
                "     but: named NotNamedLikeThat was T"));

        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().named("NotNamedLikeThat")));
    }

    @Test
    public void bound() throws Exception {
        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().withBound(Serializable.class)));
    }

    @Test
    public void bound_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is type variable(with bound java.io.Closeable)\n" +
                "     but: with bound java.io.Closeable was java.io.Serializable"));

        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().withBound(Closeable.class)));
    }

    @Test
    public void nameAndBound() throws Exception {
        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().named("T").withBound(Serializable.class)));
    }

    @Test
    public void nameAndBound_failureOnName() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is type variable(named NotQuite and with bound java.io.Serializable)\n" +
                "     but: named NotQuite was T"));

        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().named("NotQuite").withBound(Serializable.class)));
    }

    @Test
    public void nameAndBound_failureOnBound() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is type variable(named T and with bound java.io.Closeable)\n" +
                "     but: with bound java.io.Closeable was java.io.Serializable"));

        TypeVariable typeParam = ParametrizedType.class.getTypeParameters()[0];

        assertThat(typeParam, is(new TypeMatcher().named("T").withBound(Closeable.class)));
    }
}