package org.codingmatters.tests.reflect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Closeable;

import static org.codingmatters.tests.reflect.ReflectMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 9/6/16.
 */
public class ClassMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isAClass() throws Exception {
        assertThat(String.class, is(aClass()));
    }
    @Test
    public void isAnInterface() throws Exception {
        assertThat(Comparable.class, is(anInterface()));
    }

    @Test
    public void classWithName() throws Exception {
        assertThat(String.class, is(aClass().named("java.lang.String")));
    }

    @Test
    public void classWithName_failure() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(String.class, is(aClass().named("not.that.Name")));
    }

    @Test
    public void classWithMethod() throws Exception {
        assertThat(String.class, aClass().with(aMethod().named("toUpperCase")));
    }

    @Test
    public void classWithMethod_failure() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(String.class, is(aClass().with(aMethod().named("noSuchMeth"))));
    }

    @Test
    public void classWithManyMethod() throws Exception {
        assertThat(String.class, is(aClass()
                .with(aMethod().named("toUpperCase"))
                .with(aMethod().named("toLowerCase")))
        );
    }

    @Test
    public void classWithManyMethod_failure() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(String.class, is(aClass()
                .with(aMethod().named("toUpperCase"))
                .with(aMethod().named("noSuchMethod")))
        );
    }

    public class Public {}
    private class Private {}
    protected class Protected {}
    class PackagePrivate {}

    @Test
    public void publicClass() throws Exception {
    }

    @Test
    public void public_failsOnPackagePrivateClass() throws Exception {
        exception.expect(AssertionError.class);

        assertThat(PackagePrivate.class, is(aClass().public_()));
    }

    @Test
    public void privateClass() throws Exception {
        assertThat(Private.class, is(aClass().private_()));
    }

    @Test
    public void protectedClass() throws Exception {
        assertThat(Protected.class, is(aClass().protected_()));
    }

    @Test
    public void packagePrivateClass() throws Exception {
        assertThat(PackagePrivate.class, is(aClass().packagePrivate()));
    }

    static class Static {}
    class NotStatic {}

    @Test
    public void staticClass() throws Exception {
        assertThat(Static.class, is(aClass().static_()));
    }

    @Test
    public void notStaticClass() throws Exception {
        assertThat(NotStatic.class, is(aClass().instance_()));
    }

    static class ClassWithField {
        public String field;
    }

    @Test
    public void classWithField() throws Exception {
        assertThat(ClassWithField.class, is(aClass().with(aField())));
    }

    @Test
    public void classWithNamedField() throws Exception {
        assertThat(ClassWithField.class, is(aClass().with(aField().named("field"))));
    }

    interface Interface {}
    class Implementation implements Interface{}

    @Test
    public void classImplementingInterface() throws Exception {
        assertThat(Implementation.class, is(aClass().implementing(Interface.class)));
    }

    @Test
    public void classNotImplementingAnInterface() throws Exception {
        exception.expect(AssertionError.class);
        assertThat(Implementation.class, is(aClass().implementing(Closeable.class)));
    }

    @Test
    public void classImplementingAsNonInterface() throws Exception {
        exception.expect(AssertionError.class);
        assertThat(Implementation.class, is(aClass().implementing(String.class)));
    }

    class SuperClass {}
    class SubClass extends SuperClass{}

    @Test
    public void classExtendingClass() throws Exception {
        assertThat(SubClass.class, is(aClass().extending(SuperClass.class)));
    }

    @Test
    public void classNotExtendingClass() throws Exception {
        exception.expect(AssertionError.class);
        assertThat(SuperClass.class, is(aClass().extending(SubClass.class)));
    }

    static public final class FinalClass {}

    @Test
    public void finalClass() throws Exception {
        assertThat(FinalClass.class, is(aClass().final_()));

    }
}