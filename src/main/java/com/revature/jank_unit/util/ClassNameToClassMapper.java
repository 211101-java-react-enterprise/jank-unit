package com.revature.jank_unit.util;

import java.util.function.Function;

public class ClassNameToClassMapper implements Function<String, Class<?>> {

    private static final ClassNameToClassMapper instance = new ClassNameToClassMapper();

    private ClassNameToClassMapper() {
        super();
    }

    public static ClassNameToClassMapper getInstance() {
        return instance;
    }

    @Override
    public Class<?> apply(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new Error(e.getMessage(), e);
        }
    }
}
