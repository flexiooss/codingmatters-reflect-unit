package org.codingmatters.tests.reflect.utils;

/**
 * Created by nelt on 9/21/16.
 */
public class ReflectMatcherConfiguration {

    private AccessModifier accessModifier = AccessModifier.PUBLIC;
    private LevelModifier levelModifier = LevelModifier.INSTANCE;


    public ReflectMatcherConfiguration accessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
        return this;
    }

    public ReflectMatcherConfiguration levelModifier(LevelModifier levelModifier) {
        this.levelModifier = levelModifier;
        return this;
    }

    public AccessModifier accessModifier() {
        return accessModifier;
    }

    public LevelModifier levelModifier() {
        return levelModifier;
    }


}
