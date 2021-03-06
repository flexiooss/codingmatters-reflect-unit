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

        public final String finalField = "val";
    }

    @Test
    public void isAField() throws Exception {
        assertThat(field("name"), is(aField()));
        assertThat(field("name"), is(aPublic().field()));
        assertThat(field("name"), is(anInstance().field()));
        assertThat(field("name"), is(aPublic().instance().field()));
    }

    @Test
    public void namedField() throws Exception {
        assertThat(field("name"), is(aPublic().field().named("name")));
    }

    @Test
    public void fieldType() throws Exception {
        assertThat(field("name"), is(aPublic().field().withType(String.class)));
    }

    @Test
    public void fieldTypeWithTypeMatcher() throws Exception {
        assertThat(field("name"), is(aPublic().field().withType(classType(String.class))));
    }

    @Test
    public void publicField() throws Exception {
        assertThat(field("publicField"), is(aPublic().field()));
    }

    @Test
    public void privateField() throws Exception {
        assertThat(field("privateField"), is(aPrivate().field()));
    }

    @Test
    public void protectedField() throws Exception {
        assertThat(field("protectedField"), is(aProtected().field()));
    }

    @Test
    public void packagePrivateField() throws Exception {
        assertThat(field("packagePrivateField"), is(aPackagePrivate().field()));
    }

    @Test
    public void finalField() throws Exception {
        assertThat(field("finalField"), is(aPublic().field().final_()));
    }

    @Test
    public void instanceField() throws Exception {
        assertThat(field("instanceField"), is(anInstance().field()));
    }

    @Test
    public void staticField() throws Exception {
        assertThat(field("staticField"), is(aStatic().field()));
    }



    private Field field(String name) throws NoSuchFieldException {
        return TestClass.class.getDeclaredField(name);
    }
}
