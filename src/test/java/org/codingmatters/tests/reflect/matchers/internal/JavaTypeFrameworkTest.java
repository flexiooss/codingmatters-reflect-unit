package org.codingmatters.tests.reflect.matchers.internal;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 10/19/16.
 */
public class JavaTypeFrameworkTest {
    @Test
    public void genericClass() throws Exception {
        assertThat(List.class, not(instanceOf(ParameterizedType.class)));
        assertThat(List.class.getTypeParameters(), is(Matchers.arrayWithSize(1)));
        assertThat(List.class.getTypeParameters()[0], isA(TypeVariable.class));
        assertThat(List.class.getTypeParameters()[0].getName(), is("E"));
        assertThat(List.class.getTypeParameters()[0].getBounds(), is(new Type[] {Object.class}));
        assertThat(List.class.getTypeParameters()[0].getGenericDeclaration(), is((GenericDeclaration)List.class));
    }

    @Test
    public void notGenericClass() throws Exception {
        assertThat(String.class.getTypeParameters(), is(Matchers.arrayWithSize(0)));
    }

    @Test
    public void genericMethodReturnType() throws Exception {
        //Iterator<E> iterator()
        Method aGenericMethod = List.class.getMethod("iterator");
        assertThat(aGenericMethod.getReturnType(), not(instanceOf(ParameterizedType.class)));
        assertThat(aGenericMethod.getGenericReturnType(), is(instanceOf(ParameterizedType.class)));
        assertThat(aGenericMethod.getGenericReturnType(), is(not((Type)aGenericMethod.getReturnType())));

        ParameterizedType parameterizedType = (ParameterizedType) aGenericMethod.getGenericReturnType();
        assertThat(parameterizedType.getRawType(), is((Type)aGenericMethod.getReturnType()));

        assertThat(parameterizedType.getActualTypeArguments()[0], is(instanceOf(TypeVariable.class)));
        assertThat(((TypeVariable)parameterizedType.getActualTypeArguments()[0]).getName(), is("E"));
        assertThat(((TypeVariable)parameterizedType.getActualTypeArguments()[0]).getBounds(), is(new Type[]{Object.class}));
        assertThat(((TypeVariable)parameterizedType.getActualTypeArguments()[0]).getGenericDeclaration(), is((GenericDeclaration)List.class));
    }

    @Test
    public void notGenericMethodReturnType() throws Exception {
        //public String toString()
        Method notGenericMethod = String.class.getMethod("toString");
        assertThat(notGenericMethod.getReturnType(), is(notNullValue()));
        assertThat(notGenericMethod.getGenericReturnType(), is(not(instanceOf(ParameterizedType.class))));
        assertThat(notGenericMethod.getGenericReturnType(), is((Type)notGenericMethod.getReturnType()));
    }
}
