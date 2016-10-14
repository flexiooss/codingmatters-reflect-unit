package org.codingmatters.tests.reflect.matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;

import static org.codingmatters.tests.reflect.ReflectMatchers.aGenericType;
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
        assertThat(ParametrizedType.class, is(new GenericTypeMatcher()));
        assertThat(ParametrizedType.class, is(new GenericTypeMatcher().withTypeParameter(aGenericType().named("T").withBound(Serializable.class))));
    }

    @Test
    public void notParametrizedType_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (generic)\n" +
                "     but: generic org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$NotParametrizedType is not generic"));

        assertThat(NotParametrizedType.class, is(new GenericTypeMatcher()));
    }
}