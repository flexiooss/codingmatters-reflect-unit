package org.codingmatters.tests.compile.helpers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ObjectHelperTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();
    private ClassLoaderHelper classes = ClassLoaderHelper.current();

    @Test
    public void invokeWithoutArgs() throws Exception {
        assertThat(
                this.classes.wrap("yopyop").call("toString").get(),
                is("yopyop")
        );
    }

    @Test
    public void invokeWithArgs() throws Exception {
        assertThat(
                ClassLoaderHelper.current().wrap("yopyop").call("substring", int.class).with(3).get(),
                is("yop")
        );
    }

    @Test
    public void casted() throws Exception {
        Object o = SimpleDateFormat.class.getConstructor(String.class).newInstance("yyyy");
        assertThat(
                ClassLoaderHelper.current().wrap(o).as(DateFormat.class).call("format", Date.class).with(new Date()).get(),
                is("2017")
        );

        this.expected.expect(AssertionError.class);
        ClassLoaderHelper.current().wrap(o).as(Object.class).call("format", Date.class).with(new Date()).get();
    }

    @Test
    public void castedAsString() throws Exception {
        Object o = SimpleDateFormat.class.getConstructor(String.class).newInstance("yyyy");
        assertThat(
                ClassLoaderHelper.current().wrap(o).as("java.text.DateFormat").call("format", Date.class).with(new Date()).get(),
                is("2017")
        );

        this.expected.expect(AssertionError.class);
        ClassLoaderHelper.current().wrap(o).as("java.lang.Object").call("format", Date.class).with(new Date()).get();
    }
}