package org.codingmatters.tests.reflect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Closeable;
import java.io.Serializable;

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
        assertThat(String.class, is(anInstance().class_()));
    }
    
    @Test
    public void isAnInterface() throws Exception {
        assertThat(Comparable.class, is(anInstance().interface_()));
        assertThat(Comparable.class, is(anInterface()));
        assertThat(Comparable.class, is(aPublic().interface_()));
        assertThat(Comparable.class, is(anInstance().interface_()));
    }

    @Test
    public void classWithName() throws Exception {
        assertThat(String.class, is(anInstance().class_().named("java.lang.String")));
    }

    @Test
    public void classWithName_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and named not.that.Name)\n" +
                "     but: named not.that.Name was java.lang.String"));

        assertThat(String.class, is(anInstance().class_().named("not.that.Name")));
    }

    @Test
    public void classWithMethod() throws Exception {
        assertThat(String.class, anInstance().class_().with(anInstance().method().named("toUpperCase")));
    }

    @Test
    public void classWithMethod_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and method(instance and public and named noSuchMeth))\n" +
                "     but: method(instance and public and named noSuchMeth) not found"));

        assertThat(String.class, is(anInstance().class_().with(anInstance().method().named("noSuchMeth"))));
    }

    @Test
    public void classWithManyMethod() throws Exception {
        assertThat(String.class, is(anInstance().class_()
                .with(anInstance().method().named("toUpperCase"))
                .with(anInstance().method().named("toLowerCase")))
        );
    }

    @Test
    public void classWithManyMethod_failure() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and method(instance and public and named toUpperCase) and method(instance and public and named noSuchMethod))\n" +
                "     but: method(instance and public and named noSuchMethod) not found"));

        assertThat(String.class, is(anInstance().class_()
                .with(anInstance().method().named("toUpperCase"))
                .with(anInstance().method().named("noSuchMethod")))
        );
    }

    public class Public {}
    private class Private {}
    protected class Protected {}
    class PackagePrivate {}
    public final class FinalClass {}

    @Test
    public void publicClass() throws Exception {
        assertThat(Public.class, is(aClass()));
        assertThat(Public.class, is(aPublic().class_()));
        assertThat(Public.class, is(anInstance().public_().class_()));
    }

    @Test
    public void public_failsOnPackagePrivateClass() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public)\n" +
                "     but: public was package private"));

        assertThat(PackagePrivate.class, is(aPublic().class_()));
    }

    @Test
    public void privateClass() throws Exception {
        assertThat(Private.class, is(aPrivate().class_()));
    }

    @Test
    public void protectedClass() throws Exception {
        assertThat(Protected.class, is(anInstance().protected_().class_()));
    }

    @Test
    public void packagePrivateClass() throws Exception {
        assertThat(PackagePrivate.class, is(aPackagePrivate().class_()));
    }

    public static class Static {}
    public class NotStatic {}

    @Test
    public void staticClass() throws Exception {
        assertThat(Static.class, is(aStatic().class_()));
    }

    @Test
    public void notStaticClass() throws Exception {
        assertThat(NotStatic.class, is(anInstance().class_()));
    }

    public static class ClassWithField {
        public String field;
    }

    @Test
    public void classWithField() throws Exception {
        assertThat(ClassWithField.class, is(aStatic().class_().with(aPublic().field())));
    }

    @Test
    public void classWithNamedField() throws Exception {
        assertThat(ClassWithField.class, is(aStatic().class_().with(aPublic().field().named("field"))));
    }

    interface Interface {}
    public class Implementation implements Interface{}

    @Test
    public void classImplementingInterface() throws Exception {
        assertThat(Implementation.class, is(anInstance().class_().implementing(Interface.class)));
    }

    @Test
    public void classNotImplementingAnInterface() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and implements java.io.Closeable)\n" +
                "     but: implements java.io.Closeable was false (org.codingmatters.tests.reflect.ClassMatcherTest$Implementation implements [interface org.codingmatters.tests.reflect.ClassMatcherTest$Interface])"));

        assertThat(Implementation.class, is(anInstance().class_().implementing(Closeable.class)));
    }

    @Test
    public void classImplementingAsNonInterface() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and implements java.lang.String)\n" +
                "     but: implements java.lang.String was false (org.codingmatters.tests.reflect.ClassMatcherTest$Implementation implements [interface org.codingmatters.tests.reflect.ClassMatcherTest$Interface])"));

        assertThat(Implementation.class, is(anInstance().class_().implementing(String.class)));
    }

    public class SuperClass {}
    public class SubClass extends SuperClass{}

    @Test
    public void classExtendingClass() throws Exception {
        assertThat(SubClass.class, is(anInstance().class_().extending(SuperClass.class)));
    }

    @Test
    public void classNotExtendingClass() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is("\n" +
                "Expected: is (class and instance and public and extends org.codingmatters.tests.reflect.ClassMatcherTest$SubClass)\n" +
                "     but: extends org.codingmatters.tests.reflect.ClassMatcherTest$SubClass was false"));

        assertThat(SuperClass.class, is(anInstance().class_().extending(SubClass.class)));
    }

    @Test
    public void finalClass() throws Exception {
        assertThat(FinalClass.class, is(aClass().final_()));

    }

    public class ParametrizedType<T extends Serializable> {}

    @Test
    public void typeVariable() throws Exception {
        assertThat(ParametrizedType.class, is(aClass().with(aGenericType().named("T").withBound(Serializable.class))));
    }
}