package org.codingmatters.tests.reflect.matchers.internal;

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
public class TypeInfoMatcherTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isClass() throws Exception {
        assertThat(TypeInfo.from(Integer.class), is(TypeInfoMatcher.class_(Integer.class)));
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.class_(List.class)));
    }

    @Test
    public void isGeneric() throws Exception {
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic()));
    }

    @Test
    public void isGeneric_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a generic type)\n" +
                "     but: a generic type \"java.lang.String\" was not generic");

        assertThat(TypeInfo.from(String.class), is(TypeInfoMatcher.generic()));
    }

    @Test
    public void isNotGeneric() throws Exception {
        assertThat(TypeInfo.from(String.class), is(TypeInfoMatcher.nonGeneric()));
    }

    @Test
    public void isNotGenericClass_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a non generic type)\n" +
                "     but: a non generic type \"java.util.List\" was generic");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.nonGeneric()));
    }

    @Test
    public void isANonGenericClass() throws Exception {
        assertThat(TypeInfo.from(String.class), is(TypeInfoMatcher.nonGeneric().baseClass(String.class)));
    }

    @Test
    public void isANonGenericClassFailure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a non generic type and base class <class java.lang.Integer>)\n" +
                "     but: base class <class java.lang.Integer>");

        assertThat(TypeInfo.from(String.class), is(TypeInfoMatcher.nonGeneric().baseClass(Integer.class)));
    }

    @Test
    public void isAGenericClass() throws Exception {
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().baseClass(List.class)));
    }

    @Test
    public void isAGenericClassFailure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a generic type and base class <class java.lang.Integer>)\n" +
                "     but: base class <class java.lang.Integer>");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().baseClass(Integer.class)));
    }

    @Test
    public void isAVariable() throws Exception {
        assertThat(TypeInfo.from(List.class.getTypeParameters()[0]), is(TypeInfoMatcher.variable()));
    }

    @Test
    public void isAVariable_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a variable type)\n" +
                "     but: a variable type \"java.util.List\" was not a variable");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.variable()));
    }

    @Test
    public void variableName() throws Exception {
        assertThat(TypeInfo.from(List.class.getTypeParameters()[0]), is(TypeInfoMatcher.variable().named("E")));
    }

    @Test
    public void variableName_failure() throws Exception {
        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("\n" +
                "Expected: is (a variable type and named \"T\")\n" +
                "     but: named \"T\" name was \"E\"");

        assertThat(TypeInfo.from(List.class.getTypeParameters()[0]), is(TypeInfoMatcher.variable().named("T")));
    }

    @Test
    public void parameterCount() throws Exception {
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameterCount(1)));
    }

    @Test
    public void parameterCount_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and has 0 parameters)\n" +
                "     but: has 0 parameters had 1 parameters");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameterCount(0)));
    }

    @Test
    public void noParameter() throws Exception {
        assertThat(TypeInfo.from(String.class), is(TypeInfoMatcher.nonGeneric().withParameters()));
    }

    @Test
    public void oneParameter() throws Exception {
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameters(new TypeParameterMatcher())));
    }

    @Test
    public void twoParameter_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0() and parameter 1())\n" +
                "     but: parameter 1() was null");
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameters(new TypeParameterMatcher(), new TypeParameterMatcher())));
    }

    @Test
    public void oneParameterName() throws Exception {
        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameters(new TypeParameterMatcher().named("E"))));
    }

    @Test
    public void oneParameterName_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0(named \"T\"))\n" +
                "     but: parameter 0(named \"T\") named \"T\" was \"E\"");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameters(new TypeParameterMatcher().named("T"))));
    }

    interface GenericTypeWithBounds<T extends Closeable> {}

    @Test
    public void parameterWithBound() throws Exception {
        assertThat(TypeInfo.from(GenericTypeWithBounds.class), is(TypeInfoMatcher.generic().withParameters(
                new TypeParameterMatcher().upperBound(TypeInfoMatcher.nonGeneric().baseClass(Closeable.class)))
        ));
    }

    @Test
    public void parameterWithBound_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is (a generic type and parameter 0(with upper bound 0 (a non generic type and base class <interface java.io.Closeable>)))\n" +
                "     but: parameter 0(with upper bound 0 (a non generic type and base class <interface java.io.Closeable>)) with upper bound 0 (a non generic type and base class <interface java.io.Closeable>) was base class <interface java.io.Closeable> was <class java.lang.Object>");

        assertThat(TypeInfo.from(List.class), is(TypeInfoMatcher.generic().withParameters(
                new TypeParameterMatcher().upperBound(TypeInfoMatcher.nonGeneric().baseClass(Closeable.class)))
        ));
    }

}