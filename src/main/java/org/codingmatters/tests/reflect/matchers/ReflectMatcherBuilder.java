package org.codingmatters.tests.reflect.matchers;

import org.codingmatters.tests.reflect.matchers.impl.ClassMatcherImpl;
import org.codingmatters.tests.reflect.matchers.impl.ConstructorMatcherImpl;
import org.codingmatters.tests.reflect.matchers.impl.FieldMatcherImpl;
import org.codingmatters.tests.reflect.matchers.impl.MethodMatcherImpl;
import org.codingmatters.tests.reflect.matchers.support.AccessModifier;
import org.codingmatters.tests.reflect.matchers.support.LevelModifier;
import org.codingmatters.tests.reflect.matchers.support.ReflectMatcherConfiguration;

/**
 * Created by nelt on 9/21/16.
 */
public class ReflectMatcherBuilder {
    private final ReflectMatcherConfiguration configuration = new ReflectMatcherConfiguration();

    public ReflectMatcherBuilder static_() {
        this.configuration.levelModifier(LevelModifier.STATIC);
        return this;
    }

    public ReflectMatcherBuilder instance() {
        this.configuration.levelModifier(LevelModifier.INSTANCE);
        return this;
    }

    public ReflectMatcherBuilder public_() {
        this.configuration.accessModifier(AccessModifier.PUBLIC);
        return this;
    }

    public ReflectMatcherBuilder private_() {
        this.configuration.accessModifier(AccessModifier.PRIVATE);
        return this;
    }

    public ReflectMatcherBuilder protected_() {
        this.configuration.accessModifier(AccessModifier.PROTECTED);
        return this;
    }

    public ReflectMatcherBuilder packagePrivate() {
        this.configuration.accessModifier(AccessModifier.PACKAGE_PRIVATE);
        return this;
    }

    public ClassMatcher class_() {
        return ClassMatcherImpl.aClass(this.configuration);
    }

    public ClassMatcher interface_() {
        return ClassMatcherImpl.anInterface(this.configuration);
    }

    public FieldMatcher field() {
        return FieldMatcherImpl.aField(this.configuration);
    }

    public MethodMatcherImpl method() {
        return MethodMatcherImpl.aMethod(this.configuration);
    }

    public ConstructorMatcher constructor() {
        return ConstructorMatcherImpl.aConstructor(this.configuration);
    }
}
