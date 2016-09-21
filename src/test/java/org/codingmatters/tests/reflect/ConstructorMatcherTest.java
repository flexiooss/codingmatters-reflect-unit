package org.codingmatters.tests.reflect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;

import static org.codingmatters.tests.reflect.ReflectMatchers.aConstructor;
import static org.codingmatters.tests.reflect.ReflectMatchers.aStatic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 9/15/16.
 */
public class ConstructorMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    static public class TestClass {
        private TestClass() {}
        public TestClass(String arg) {}
    }

    @Test
    public void publicConstructor() throws Exception {
        assertThat(constructor(String.class), is(aConstructor().public_()));
    }

    @Test
    public void privateConstructor() throws Exception {
        assertThat(constructor(), is(aConstructor().private_()));
    }

    @Test
    public void constructorWithParameters() throws Exception {
        assertThat(constructor(String.class), is(aConstructor().withParameters(String.class)));
    }

    @Test
    public void classConstructor() throws Exception {
        assertThat(TestClass.class, is(aStatic().class_().with(aConstructor().withParameters(String.class))));
    }

    private Constructor constructor(Class ... args) throws NoSuchMethodException {
        return TestClass.class.getDeclaredConstructor(args);
    }
}