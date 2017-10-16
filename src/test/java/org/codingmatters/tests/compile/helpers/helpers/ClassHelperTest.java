package org.codingmatters.tests.compile.helpers.helpers;

import org.codingmatters.tests.compile.helpers.ClassLoaderHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ClassHelperTest {

    private ClassLoaderHelper classes = ClassLoaderHelper.current();

    @Test
    public void array() throws Exception {
        ClassArrayHelper strArray = classes.get(String.class.getName()).array();

        assertThat(strArray.get().getName(), is("[Ljava.lang.String;"));
        assertThat((String []) strArray.newArray().get(), is(emptyArray()));
        assertThat((String []) strArray.newArray("A", "B").get(), is(arrayContaining("A", "B")));
    }
}