package org.codingmatters.tests.reflect.matchers.internal;

import org.codingmatters.tests.reflect.matchers.type.TypeInfo;
import org.codingmatters.tests.reflect.matchers.type.TypeParameterInfo;
import org.junit.Test;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 10/20/16.
 */
public class TypeInfoTest {

    public interface Generic<T> {
        Generic<T> genericReturnType();
        Object notGenericReturnType();
        T typeVariableReturnType();
        List<? extends T> wildcardBoundedToTypeParameter();
    }
    public interface NotGeneric {}
    public interface NotGenericExtension extends Generic<Object> {}


    @Test
    public void name() throws Exception {
        assertThat(TypeInfo.from(Generic.class).name(), is(
                "org.codingmatters.tests.reflect.matchers.internal.TypeInfoTest$Generic"
        ));
        assertThat(TypeInfo.from(NotGeneric.class).name(), is(
                "org.codingmatters.tests.reflect.matchers.internal.TypeInfoTest$NotGeneric"
        ));
        assertThat(TypeInfo.from(NotGenericExtension.class).name(), is(
                "org.codingmatters.tests.reflect.matchers.internal.TypeInfoTest$NotGenericExtension"
        ));
        assertThat(TypeInfo.from(BoundedGenericWithParametrizedTypeBound.class).name(), is(
                "org.codingmatters.tests.reflect.matchers.internal.TypeInfoTest$BoundedGenericWithParametrizedTypeBound"
        ));
        assertThat(TypeInfo.from(Generic.class.getMethod("genericReturnType").getGenericReturnType()).name(), is(
                "org.codingmatters.tests.reflect.matchers.internal.TypeInfoTest$Generic"
        ));
    }



    @Test
    public void genericClass() throws Exception {
        assertThat(TypeInfo.from(Generic.class).isGeneric(), is(true));
        assertThat(TypeInfo.from(Generic.class).isVariable(), is(false));
        assertThat(TypeInfo.from(Generic.class).baseClass(), is((Type)Generic.class));
    }

    interface GenericTypeWithBounds<T extends Closeable & Serializable> {}

    @Test
    public void genericClassWithBounds() throws Exception {
        TypeParameterInfo typeParameter = TypeInfo.from(GenericTypeWithBounds.class).parameters().get(0);
        assertThat(typeParameter.name(), is("T"));
        assertThat(typeParameter.upperBounds().get(0).name(), is("java.io.Closeable"));
        assertThat(typeParameter.upperBounds().get(1).name(), is("java.io.Serializable"));
        assertThat(typeParameter.upperBounds(), hasSize(2));
    }

    public interface BoundedGenericWithParametrizedTypeBound<T extends Integer & Comparable<Integer>> {}

    @Test
    public void boundedGenericWithParametrizedTypeBound() throws Exception {
        assertThat(TypeInfo.from(BoundedGenericWithParametrizedTypeBound.class).isGeneric(), is(true));
        assertThat(TypeInfo.from(BoundedGenericWithParametrizedTypeBound.class).isVariable(), is(false));

        TypeParameterInfo typeParameter = TypeInfo.from(BoundedGenericWithParametrizedTypeBound.class).parameters().get(0);

        assertThat(typeParameter.upperBounds().get(0).name(), is("java.lang.Integer"));
        assertThat(typeParameter.upperBounds().get(1).name(), is("java.lang.Comparable"));
        assertThat(typeParameter.upperBounds(), hasSize(2));

        assertThat(typeParameter.upperBounds().get(1).parameters().get(0).name(), is("java.lang.Integer"));
        assertThat(typeParameter.upperBounds().get(1).parameters(), hasSize(1));
    }

    @Test
    public void notGenericClass() throws Exception {
        assertThat(TypeInfo.from(NotGeneric.class).isGeneric(), is(false));
        assertThat(TypeInfo.from(NotGeneric.class).isVariable(), is(false));

        assertThat(TypeInfo.from(NotGenericExtension.class).isGeneric(), is(false));
        assertThat(TypeInfo.from(NotGenericExtension.class).isVariable(), is(false));
    }

    @Test
    public void genericReturnType() throws Exception {
        TypeInfo type = TypeInfo.from(Generic.class.getMethod("genericReturnType").getGenericReturnType());
        assertThat(type.isGeneric(), is(true));
        assertThat(type.isVariable(), is(false));
    }

    @Test
    public void notGenericReturnType() throws Exception {
        TypeInfo type = TypeInfo.from(Generic.class.getMethod("notGenericReturnType").getGenericReturnType());
        assertThat(type.isGeneric(), is(false));
        assertThat(type.isVariable(), is(false));
    }

    @Test
    public void typeVariableReturnType() throws Exception {
        TypeInfo type = TypeInfo.from(Generic.class.getMethod("typeVariableReturnType").getGenericReturnType());
        assertThat(type.isGeneric(), is(false));
        assertThat(type.isVariable(), is(true));
    }

    static public List<? extends Serializable> upperBoundedWildcardReturnType() {return null;}
    static public List<? super Serializable> lowerBoundedWildcardReturnType() {return null;}

    @Test
    public void wildcardReturnType() throws Exception {
        TypeInfo type = TypeInfo.from(this.getClass().getMethod("upperBoundedWildcardReturnType").getGenericReturnType());
        assertThat(type.isGeneric(), is(true));
        assertThat(type.isVariable(), is(false));

        assertThat(type.parameters().get(0).name(), is("?"));
        assertThat(type.parameters().get(0).isWildcard(), is(true));
        assertThat(type.parameters().get(0).upperBounds().get(0).baseClass(), is((Type)Serializable.class));
    }

    @Test
    public void wildcardWithVariableBound() throws Exception {
        //java.util.function.Consumer<? super T>
        TypeInfo type = TypeInfo.from(Iterable.class.getMethod("forEach", Consumer.class).getGenericParameterTypes()[0]);

        assertThat(type.parameters().get(0).name(), is("?"));
        assertThat(type.parameters().get(0).isWildcard(), is(true));
        assertThat(type.parameters().get(0).lowerBounds().get(0).isVariable(), is(true));
        assertThat(type.parameters().get(0).lowerBounds().get(0).name(), is("T"));
    }

    @Test
    public void wildcardReturnType_withUpperBound() throws Exception {
        TypeInfo type = TypeInfo.from(this.getClass().getMethod("upperBoundedWildcardReturnType").getGenericReturnType());

        assertThat(type.parameters().get(0).upperBounds().get(0).name(), is(Serializable.class.getName()));
        assertThat(type.parameters().get(0).upperBounds(), hasSize(1));
    }

    @Test
    public void wildcardReturnType_withLowerBound() throws Exception {
        TypeInfo type = TypeInfo.from(this.getClass().getMethod("lowerBoundedWildcardReturnType").getGenericReturnType());

        assertThat(type.parameters().get(0).lowerBounds().get(0).name(), is(Serializable.class.getName()));
        assertThat(type.parameters().get(0).lowerBounds(), hasSize(1));
    }

    @Test
    public void wildcardBoundedToTypeParameter() throws Exception {
        //List<? extends T> wildcardBoundedToTypeParameter();
        TypeInfo type = TypeInfo.from(Generic.class.getMethod("wildcardBoundedToTypeParameter").getGenericReturnType());

        assertThat(type.parameters().get(0).upperBounds().get(0).isVariable(), is(true));
        assertThat(type.parameters().get(0).upperBounds().get(0).name(), is("T"));
    }

    @Test
    public void parameterTypesFromClass() throws Exception {
        TypeInfo type = TypeInfo.from(Generic.class);

        List<TypeParameterInfo> params = type.parameters();
        assertThat(params, hasSize(1));
        assertThat(params.get(0).name(), is("T"));
    }

    @Test
    public void parameterTypesFromReturnType() throws Exception {
        TypeInfo type = TypeInfo.from(Generic.class.getMethod("genericReturnType").getGenericReturnType());

        List<TypeParameterInfo> params = type.parameters();
        assertThat(params, hasSize(1));
        assertThat(params.get(0).name(), is("T"));
    }

    @Test
    public void equality() throws Exception {
        assertThat(TypeInfo.from(String.class), is(TypeInfo.from(String.class)));
        assertThat(TypeInfo.from(String.class), is(not(TypeInfo.from(Integer.class))));
    }

    @Test
    public void list() throws Exception {
        List<TypeParameterInfo> parameters = TypeInfo.from(List.class).parameters();
        assertThat(parameters, hasSize(1));
    }
}
