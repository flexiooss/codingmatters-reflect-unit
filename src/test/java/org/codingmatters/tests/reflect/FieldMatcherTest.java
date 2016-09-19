package org.codingmatters.tests.reflect;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.codingmatters.tests.reflect.ReflectMatchers.aStatic_;
import static org.codingmatters.tests.reflect.ReflectMatchers.anInstance;
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
        assertThat(field("name"), is(FieldMatcher.anInstanceField().named("name")));
    }

    @Test
    public void fieldType() throws Exception {
        assertThat(field("name"), is(FieldMatcher.anInstanceField().withType(String.class)));
    }

    @Test
    public void publicField() throws Exception {
        assertThat(field("publicField"), is(FieldMatcher.anInstanceField().public_()));
    }

    @Test
    public void privateField() throws Exception {
        assertThat(field("privateField"), is(FieldMatcher.anInstanceField().private_()));
    }

    @Test
    public void protectedField() throws Exception {
        assertThat(field("protectedField"), is(FieldMatcher.anInstanceField().protected_()));
    }

    @Test
    public void packagePrivateField() throws Exception {
        assertThat(field("packagePrivateField"), is(FieldMatcher.anInstanceField().packagePrivate()));
    }

    @Test
    public void finalField() throws Exception {
        assertThat(field("finalField"), is(FieldMatcher.anInstanceField().final_()));
    }

    @Test
    public void instanceField() throws Exception {
        assertThat(field("instanceField"), is(anInstance().field()));
    }

    @Test
    public void staticField() throws Exception {
        assertThat(field("staticField"), is(aStatic_().field()));
    }



    private Field field(String name) throws NoSuchFieldException {
        return TestClass.class.getDeclaredField(name);
    }
}
