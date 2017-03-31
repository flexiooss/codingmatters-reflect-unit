package org.codingmatters.tests.reflect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;

import static org.codingmatters.tests.reflect.ReflectMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 9/8/16.
 */
public class MethodMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    static abstract class TestClass {
        public void publicMethod() {}
        private void privateMethod() {}
        protected void protectedMethod() {}
        void packagePrivateMethod() {}

        public String returnsString() {return "";}
        public void returnsVoid() {}
        public String[] returnsArray() {return null;}

        public void withParameters(String s, int i) {}
        public void withoutParameters() {}
        public void withArrayParameter(String[] s) {}

        static public void staticMethod() {}
        public void instanceMethod() {}

        public abstract void abstractMethod();

        public String complete(String arg1, String arg2) {return "";}

        public final void finalMethod() {}

        @Deprecated
        public void anotated() {}

        public <T> void parametrized() {}
        public void notParametrized() {}
        public <T> void parametrizedWithParameterTypeArg(T[] t) {}
        public <T> void parametrizedWithNonParameterTypeArg(Object o) {}
        public <T> void parametrizedWithParameterTypeArrayArg(T[] t) {}
        public <T> T parametrizedReturningParameterType() {return null;}
        public <T> String parametrizedNotReturningParameterType() {return null;}
        public <T> T[] parametrizedReturningParameterTypeArray() {return null;}

        public void throwing() throws IOException {}
        public void throwingMany() throws IOException, MalformedURLException {}
        public void notThrowing() {}


    }

    @Test
    public void completeSignature() throws Exception {
        assertThat(method("complete", String.class, String.class),
                is(
                        aPublic().method().named("complete").withParameters(String.class, String.class).returning(String.class)
                )
        );
    }

    @Test
    public void isAMethod() throws Exception {
        assertThat(method("publicMethod"), is(anInstance().method()));
        assertThat(method("publicMethod"), is(aMethod()));
        assertThat(method("publicMethod"), is(aPublic().method()));
        assertThat(method("publicMethod"), is(aPublic().instance().method()));
    }

    @Test
    public void aMethodWithName() throws Exception {
        assertThat(method("publicMethod"), is(anInstance().method().named("publicMethod")));
    }

    @Test
    public void aMethodWithName_failure() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(method("publicMethod"), is(anInstance().method().named("noNamedLikeThat")));
    }

    @Test
    public void isAPublicMethod() throws Exception {
        assertThat(
                method("publicMethod"),
                is(ReflectMatchers.aPublic().method())
        );
    }

    @Test
    public void isAPublicMethod_failure() throws Exception {
        exception.expect(AssertionError.class);
        assertThat(
                method("privateMethod"),
                is(aPublic().method())
        );
    }

    @Test
    public void isAPrivateMethod() throws Exception {
        assertThat(
                method("privateMethod"),
                is(aPrivate().method())
        );
    }

    @Test
    public void isAProtectedMethod() throws Exception {
        assertThat(
                method("protectedMethod"),
                is(aProtected().method())
        );
    }

    @Test
    public void isAPackagePrivateMethod() throws Exception {
        assertThat(
                method("packagePrivateMethod"),
                is(aPackagePrivate().method())
        );
    }

    @Test
    public void returnsType() throws Exception {
        assertThat(method("returnsString"), is(anInstance().method().returning(String.class)));
    }

    @Test
    public void returnsVoid() throws Exception {
        assertThat(method("returnsVoid"), is(anInstance().method().returningVoid()));
        assertThat(method("returnsVoid"), is(anInstance().method().returning(void.class)));
    }

    @Test
    public void returnsArray() throws Exception {
        assertThat(method("returnsArray"), is(aMethod().returning(String[].class)));
    }

    @Test
    public void returnsType_failure() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(method("returnsString"), is(anInstance().method().returning(List.class)));
    }

    @Test
    public void withParameters() throws Exception {
        assertThat(method("withParameters", String.class, int.class), is(anInstance().method().withParameters(String.class, int.class)));
    }

    @Test
    public void withoutParameters() throws Exception {
        assertThat(method("withoutParameters"), is(anInstance().method().withoutParameters()));
    }

    @Test
    public void withArrayParameter() throws Exception {
        assertThat(method("withArrayParameter", String[].class), is(anInstance().method().withParameters(String[].class)));
    }

    @Test
    public void withParameters_fails() throws Exception {
        exception.expect(AssertionError.class);
        assertThat(method("withParameters", String.class, int.class), is(anInstance().method().withParameters(String.class)));

        exception.expect(AssertionError.class);
        assertThat(method("withParameters", String.class, int.class), is(anInstance().method().withoutParameters()));

        exception.expect(AssertionError.class);
        assertThat(method("withoutParameters"), is(anInstance().method().withParameters(String.class)));
    }

    @Test
    public void staticMethod() throws Exception {
        assertThat(method("staticMethod"), is(aStatic().method()));
    }

    @Test
    public void staticMethod_fails() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(method("instanceMethod"), is(aStatic().method()));
    }

    @Test
    public void instanceMethod() throws Exception {
        assertThat(method("instanceMethod"), is(anInstance().method()));
        assertThat(method("instanceMethod"), is(anInstance().method()));
    }

    @Test
    public void instanceMethod_fails() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(method("staticMethod"), is(anInstance().method()));
    }

    @Test
    public void abstractMethod() throws Exception {
        assertThat(method("abstractMethod"), is(anInstance().method().abstract_()));
    }

    @Test
    public void finalMethod() throws Exception {
        assertThat(method("finalMethod"), is(anInstance().method().final_()));
    }

    @Test
    public void anotatedMethod() throws Exception {
        assertThat(method("anotated"), is(aPublic().method().anotatedWith(Deprecated.class)));
    }

    private Method method(String name, Class ... args) throws NoSuchMethodException {
        return TestClass.class.getDeclaredMethod(name, args);
    }

    @Test
    public void parametrizedMethod() throws Exception {
        assertThat(method("parametrized"), is(aMethod().withVariable(variableType().named("T"))));
    }

    @Test
    public void parametrizedMethod_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and (a variable type and named \"T\"))\n" +
                "     but: (a variable type and named \"T\") not found"));

        assertThat(method("notParametrized"), is(aMethod().withVariable(variableType().named("T"))));
    }

    @Test
    public void parametrizedMethodReturningParameterType() throws Exception {
        assertThat(method("parametrizedReturningParameterType"), is(aMethod()
                .withVariable(variableType().named("T"))
                .returning(variableType().named("T"))
        ));
    }

    @Test
    public void parametrizedMethodReturningParameterType_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and (a variable type and named \"T\") and return ((a variable type and named \"T\")))\n" +
                "     but: return ((a variable type and named \"T\")) was (a variable type \"java.lang.String\" was not a variable)"));

        assertThat(method("parametrizedNotReturningParameterType"), is(aMethod()
                .withVariable(variableType().named("T"))
                .returning(variableType().named("T"))
        ));
    }

    @Test
    public void parametrizedWithParameterTypeArg() throws Exception {
        //public <T> void parametrizedWithParameterTypeArg(T[] t) {}
        assertThat(method("parametrizedWithParameterTypeArg", Object[].class), is(aMethod()
                .withVariable(variableType().named("T"))
                .withParameters(typeArray(variableType().named("T")))
                .returningVoid()
        ));
    }

    @Test
    public void parametrizedWithParameterTypeArg_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and (a variable type and named \"T\") and 1 parameters and parameter[0] is array of (a variable type and named \"T\") and return ((base class <void>)))\n" +
                "     but: parameter[0] is array of (a variable type and named \"T\") was a java.lang.reflect.Method (<public void org.codingmatters.tests.reflect.MethodMatcherTest$TestClass.parametrizedWithNonParameterTypeArg(java.lang.Object)>)"));

        assertThat(method("parametrizedWithNonParameterTypeArg", Object.class), is(aMethod()
                .withVariable(variableType().named("T"))
                .withParameters(typeArray(variableType().named("T")))
                .returningVoid()
        ));
    }

    @Test
    public void parametrizedReturningParameterTypeArray() throws Exception {
        assertThat(method("parametrizedReturningParameterTypeArray"), is(aMethod()
                .withVariable(variableType().named("T"))
                .returning(typeArray(variableType().named("T")))
        ));
    }

    @Test
    public void parametrizedReturningParameterTypeArray_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and return (array of (a variable type and named \"Z\")))\n" +
                "     but: return (array of (a variable type and named \"Z\")) was (array of named \"Z\" name was \"T\")"));

        assertThat(method("parametrizedReturningParameterTypeArray"), is(aMethod()
                .returning(typeArray(variableType().named("Z")))
        ));
    }

    @Test
    public void parametrizedReturningArray() throws Exception {
        assertThat(method("parametrizedReturningParameterTypeArray"), is(aMethod()
                .withVariable(variableType().named("T"))
                .returning(typeArray())
        ));
    }

    @Test
    public void returningArray_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage("\n" +
                "Expected: is method(instance and public and return (array))\n" +
                "     but: return (array) was (not an array)");

        assertThat(method("returnsString"), is(aMethod()
                .returning(typeArray())
        ));
    }

    @Test
    public void methodThrowingException() throws Exception {
        assertThat(method("throwing"), is(aMethod().throwing(IOException.class)));
    }

    @Test
    public void methodThrowingManyException() throws Exception {
        assertThat(method("throwingMany"), is(aMethod().throwing(IOException.class, MalformedURLException.class)));
    }

    @Test
    public void methodNotThrowingException() throws Exception {
        assertThat(method("notThrowing"), is(aMethod().notThrowing()));
    }


    @Test
    public void throwing_failure_whenNotThrowingAnything() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and 1 thrown exceptions and throws (base class <class java.lang.Exception>))\n" +
                "     but: 1 thrown exceptions was 0"));

        assertThat(method("notThrowing"), is(aMethod().throwing(Exception.class)));
    }

    @Test
    public void throwing_failure_whenNotThrowingExpectedExceptionType() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and 1 thrown exceptions and throws (base class <class java.net.MalformedURLException>))\n" +
                "     but: throws (base class <class java.net.MalformedURLException>) was a java.lang.reflect.Method (<public void org.codingmatters.tests.reflect.MethodMatcherTest$TestClass.throwing() throws java.io.IOException>)"));

        assertThat(method("throwing"), is(aMethod().throwing(MalformedURLException.class)));
    }

    @Test
    public void throwing_failure_whenExceptionListInWrongOrder() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is method(instance and public and 2 thrown exceptions and throws (base class <class java.net.MalformedURLException>) and throws (base class <class java.io.IOException>))\n" +
                "     but: throws (base class <class java.net.MalformedURLException>) was a java.lang.reflect.Method (<public void org.codingmatters.tests.reflect.MethodMatcherTest$TestClass.throwingMany() throws java.io.IOException,java.net.MalformedURLException>)"));

        assertThat(method("throwingMany"), is(aMethod().throwing(MalformedURLException.class, IOException.class)));
    }
}