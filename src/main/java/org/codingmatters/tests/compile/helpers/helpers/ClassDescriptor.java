package org.codingmatters.tests.compile.helpers.helpers;

public class ClassDescriptor {
    private final String className;

    public ClassDescriptor(String className) {
        this.className = className;
    }

    public String className() {
        return className;
    }

    @Override
    public String toString() {
        return "ClassDescriptor{" +
                "className='" + className + '\'' +
                '}';
    }
}
