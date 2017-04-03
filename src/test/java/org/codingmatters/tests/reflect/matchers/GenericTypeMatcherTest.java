package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.ReflectMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Spliterator;

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

    public List<String> returnListOfString() {return null;}

    @Test
    public void parametrizedType() throws Exception {
        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType()));
        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType().baseClass(ParametrizedType.class)));

        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter().named("T").upperBound(ReflectMatchers.classType(Serializable.class)))));

        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType().baseClass(ParametrizedType.class).withParameters(ReflectMatchers.typeParameter().named("T"))));
        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType().baseClass(ParametrizedType.class).withParameters(ReflectMatchers.typeParameter().named("T").upperBound(ReflectMatchers.classType(Serializable.class)))));
    }

    @Test
    public void genericTypeWithVariableType() throws Exception {
        assertThat(Iterable.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter().named("T"))));
    }

    @Test
    public void methodWithGenericArrayArgument() throws Exception {
        assertThat(List.class.getMethod("toArray", Object[].class), is(
                aMethod().withParameters(ReflectMatchers.typeArray(ReflectMatchers.variableType().named("T")))
        ));
    }

    @Test
    public void genericReturnType() throws Exception {
        // Spliterator<E> spliterator()
        assertThat(List.class.getMethod("spliterator"), is(
                aMethod().withoutParameters().returning(ReflectMatchers.genericType().baseClass(Spliterator.class))
        ));
    }

    @Test
    public void notParametrizedType_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (a generic type)\n" +
                "     but: a generic type \"org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$NotParametrizedType\" was not generic"));

        assertThat(NotParametrizedType.class, is(ReflectMatchers.genericType()));
    }

    @Test
    public void of_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (a generic type and base class <interface java.io.Serializable>)\n" +
                "     but: base class <interface java.io.Serializable> was <class org.codingmatters.tests.reflect.matchers.GenericTypeMatcherTest$ParametrizedType>"));

        assertThat(ParametrizedType.class, is(ReflectMatchers.genericType().baseClass(Serializable.class)));
    }

    @Test
    public void listOfString() throws Exception {
        ParameterizedType listOfString = (ParameterizedType) this.getClass().getMethod("returnListOfString").getGenericReturnType();
        assertThat(
                listOfString,
                is(ReflectMatchers.genericType()
                        .baseClass(List.class)
                        .withParameters(ReflectMatchers.classTypeParameter(String.class))
                )
        );
    }
}