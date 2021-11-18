package com.revature.jank_unit.util;

public class Assert {

    private Assert() {
        super();
    }

    public static void isTrue(boolean bool) {
        if (!bool) throw new AssertionException("Expected provided value to be true");
    }

    public static void isFalse(boolean bool) {
        if (bool) throw new AssertionException("Expected provided value to be false");
    }

}
