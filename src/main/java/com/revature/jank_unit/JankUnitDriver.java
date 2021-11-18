package com.revature.jank_unit;

import com.revature.jank_unit.util.AfterEach;
import com.revature.jank_unit.util.BeforeEach;
import com.revature.jank_unit.util.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class JankUnitDriver {

    public static void main(String[] args) {

        System.out.println("Running tests...");
        int passed = 0, failed = 0, error = 0;

        try {

            Class<?> testSuiteClass = Class.forName("com.revature.jank_unit.FakeServiceTestSuite");
            List<Method> testMethods = new LinkedList<>();
            Method beforeEachMethod = null;
            Method afterEachMethod = null;
            for (Method classMethod : testSuiteClass.getDeclaredMethods()) {
                if (classMethod.isAnnotationPresent(Test.class)) {
                    testMethods.add(classMethod);
                } else if (classMethod.isAnnotationPresent(BeforeEach.class)) {
                    if (beforeEachMethod != null) throw new Error("Only one @BeforeEach method expected per test suite!");
                    beforeEachMethod = classMethod;
                } else if (classMethod.isAnnotationPresent(AfterEach.class)) {
                    if (afterEachMethod != null) throw new Error("Only one @AfterEach method expected per test suite!");
                    afterEachMethod = classMethod;
                }
            }

            Object testSuiteInstance = testSuiteClass.newInstance();
            for (Method testMethod : testMethods) {
                try {
                    if (beforeEachMethod != null) beforeEachMethod.invoke(testSuiteInstance);
                    testMethod.invoke(testSuiteInstance);
                    passed++;
                } catch (InvocationTargetException e) {
                    failed++;
                } catch (Throwable t) {
                    t.printStackTrace();
                    error++;
                } finally {
                    if (afterEachMethod != null) afterEachMethod.invoke(testSuiteInstance);
                }

            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }

        System.out.printf("Passed: %d, Failed: %d, Error: %d\n", passed, failed, error);

    }

}
