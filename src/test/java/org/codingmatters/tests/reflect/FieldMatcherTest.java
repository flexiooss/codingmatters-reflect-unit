package org.codingmatters.tests.reflect;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.codingmatters.tests.reflect.ReflectMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 9/11/16.
 */
public class FieldMatcherTest {

    static public class TestClass {
        public String name;

        public String publicField;
        private String privateField;
        protected String protectedField;
        String packagePrivateField;

        public String instanceField;
        static public String staticField;

        final String finalField = "val";
    }

    @Test
    public void namedField() throws Exception {
        assertThat(field("name"), is(aField().named("name")));
    }

    @Test
    public void fieldType() throws Exception {
        assertThat(field("name"), is(aField().withType(String.class)));
    }

    @Test
    public void publicField() throws Exception {
        assertThat(field("publicField"), is(aField().public_()));
    }

    @Test
    public void privateField() throws Exception {
        assertThat(field("privateField"), is(aField().private_()));
    }

    @Test
    public void protectedField() throws Exception {
        assertThat(field("protectedField"), is(aField().protected_()));
    }

    @Test
    public void packagePrivateField() throws Exception {
        assertThat(field("packagePrivateField"), is(aField().packagePrivate()));
    }

    @Test
    public void finalField() throws Exception {
        assertThat(field("finalField"), is(aField().final_()));
    }

    @Test
    public void instanceField() throws Exception {
        assertThat(field("instanceField"), is(anInstanceField()));
    }

    @Test
    public void staticField() throws Exception {
        assertThat(field("staticField"), is(aStaticField()));
    }



    private Field field(String name) throws NoSuchFieldException {
        return TestClass.class.getDeclaredField(name);
    }
}
