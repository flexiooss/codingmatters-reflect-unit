package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.ReflectMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Closeable;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 10/26/16.
 */
public class TypeMatcherTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isClass() throws Exception {
        assertThat(Integer.class, is(ReflectMatchers.classType(Integer.class)));
        assertThat(List.class, is(ReflectMatchers.classType(List.class)));
    }

    @Test
    public void isGeneric() throws Exception {
        assertThat(List.class, is(ReflectMatchers.genericType()));
    }

    @Test
    public void isGeneric_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a generic type)\n" +
                "     but: a generic type \"java.lang.String\" was not generic");

        assertThat(String.class, is(ReflectMatchers.genericType()));
    }

    @Test
    public void isNotGeneric() throws Exception {
        assertThat(String.class, is(ReflectMatchers.nonGenericType()));
    }

    @Test
    public void isNotGenericClass_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a non generic type)\n" +
                "     but: a non generic type \"java.util.List\" was generic");

        assertThat(List.class, is(ReflectMatchers.nonGenericType()));
    }

    @Test
    public void isANonGenericClass() throws Exception {
        assertThat(String.class, is(ReflectMatchers.nonGenericType().baseClass(String.class)));
    }

    @Test
    public void isANonGenericClassFailure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a non generic type and base class <class java.lang.Integer>)\n" +
                "     but: base class <class java.lang.Integer>");

        assertThat(String.class, is(ReflectMatchers.nonGenericType().baseClass(Integer.class)));
    }

    @Test
    public void isAGenericClass() throws Exception {
        assertThat(List.class, is(ReflectMatchers.genericType().baseClass(List.class)));
    }

    @Test
    public void isAGenericClassFailure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a generic type and base class <class java.lang.Integer>)\n" +
                "     but: base class <class java.lang.Integer>");

        assertThat(List.class, is(ReflectMatchers.genericType().baseClass(Integer.class)));
    }

    @Test
    public void isAVariable() throws Exception {
        assertThat(List.class.getTypeParameters()[0], is(ReflectMatchers.variableType()));
    }

    @Test
    public void isAVariable_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a variable type)\n" +
                "     but: a variable type \"java.util.List\" was not a variable");

        assertThat(List.class, is(ReflectMatchers.variableType()));
    }

    @Test
    public void variableName() throws Exception {
        assertThat(List.class.getTypeParameters()[0], is(ReflectMatchers.variableType().named("E")));
    }

    @Test
    public void variableName_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a variable type and named \"T\")\n" +
                "     but: named \"T\" name was \"E\"");

        assertThat(List.class.getTypeParameters()[0], is(ReflectMatchers.variableType().named("T")));
    }

    @Test
    public void parameterCount() throws Exception {
        assertThat(List.class, is(ReflectMatchers.genericType().withParameterCount(1)));
    }

    @Test
    public void parameterCount_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and has 0 parameters)\n" +
                "     but: has 0 parameters had 1 parameters");

        assertThat(List.class, is(ReflectMatchers.genericType().withParameterCount(0)));
    }

    @Test
    public void noParameter() throws Exception {
        assertThat(String.class, is(ReflectMatchers.nonGenericType().withParameters()));
    }

    @Test
    public void oneParameter() throws Exception {
        assertThat(List.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter())));
    }

    @Test
    public void twoParameter_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0() and parameter 1())\n" +
                "     but: parameter 1() was null");
        assertThat(List.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter(), ReflectMatchers.typeParameter())));
    }

    @Test
    public void oneParameterName() throws Exception {
        assertThat(List.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter().named("E"))));
    }

    @Test
    public void oneParameterName_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0(named \"T\"))\n" +
                "     but: parameter 0(named \"T\") named \"T\" was \"E\"");

        assertThat(List.class, is(ReflectMatchers.genericType().withParameters(ReflectMatchers.typeParameter().named("T"))));
    }

    interface GenericTypeWithBounds<T extends Closeable> {}

    @Test
    public void parameterWithBound() throws Exception {
        assertThat(GenericTypeWithBounds.class, is(ReflectMatchers.genericType().withParameters(
                ReflectMatchers.typeParameter().upperBound(ReflectMatchers.nonGenericType().baseClass(Closeable.class)))
        ));
    }

    @Test
    public void parameterWithBound_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0(with upper bound 0 (a non generic type and base class <interface java.io.Closeable>)))\n" +
                "     but: parameter 0(with upper bound 0 (a non generic type and base class <interface java.io.Closeable>)) with upper bound 0 (a non generic type and base class <interface java.io.Closeable>) was base class <interface java.io.Closeable> was <class java.lang.Object>");

        assertThat(List.class, is(ReflectMatchers.genericType().withParameters(
                ReflectMatchers.typeParameter().upperBound(ReflectMatchers.nonGenericType().baseClass(Closeable.class)))
        ));
    }

    @Test
    public void typeArray() throws Exception {
        assertThat(
                List.class.getMethod("toArray", Object[].class).getGenericReturnType(),
                is(ReflectMatchers.typeArray())
        );
    }

    @Test
    public void typeArray_anArrayOfVariable() throws Exception {
        assertThat(
                List.class.getMethod("toArray", Object[].class).getGenericReturnType(),
                is(ReflectMatchers.typeArray(ReflectMatchers.variableType().named("T")))
        );
    }

    @Test
    public void typeArray_notAnArray_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is array of (a variable type and named \"T\")\n" +
                "     but: not an array");

        assertThat(
                List.class.getMethod("size").getGenericReturnType(),
                is(ReflectMatchers.typeArray(ReflectMatchers.variableType().named("T")))
        );
    }

    @Test
    public void typeArray_notMatchingType_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is array of (base class <class java.lang.String>)\n" +
                "     but: array of base class <class java.lang.String> was null");

        assertThat(
                List.class.getMethod("toArray", Object[].class).getGenericReturnType(),
                is(ReflectMatchers.typeArray(ReflectMatchers.classType(String.class)))
        );
    }

    @Test
    public void notGenericTypeArray() throws Exception {
        assertThat(
                List.class.getMethod("toArray").getGenericReturnType(),
                is(ReflectMatchers.typeArray(ReflectMatchers.classType(Object.class)))
        );
    }

    @Test
    public void notGenericTypeArray_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is array of (base class <class java.lang.String>)\n" +
                "     but: array of base class <class java.lang.String> was <class java.lang.Object>");

        assertThat(
                List.class.getMethod("toArray").getGenericReturnType(),
                is(ReflectMatchers.typeArray(ReflectMatchers.classType(String.class)))
        );
    }
}