package com.revature.jank_unit;

import com.revature.jank_unit.util.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JankUnitDriver {

    public static void main(String[] args) {

        List<Class<?>> testSuites = getClassNamesInPackage("com.revature").stream()
                                    .map(ClassNameToClassMapper.getInstance())
                                    .filter(clazz -> clazz.isAnnotationPresent(Describe.class))
                                    .collect(Collectors.toList());

        System.out.printf("Found %d test suite classes.\nRunning tests...\n", testSuites.size());

        List<String> passingTests = new ArrayList<>();
        List<HashMap<String, String>> failingTests = new ArrayList<>();
        List<HashMap<String, String>> errorTests = new ArrayList<>();

        System.out.println("+---------------------------------------------------------------------------------------+");

        for (Class<?> testSuite : testSuites) {

            System.out.println("Running tests for test suite: " + testSuite.getName() + "\n");

            try {
                List<Method> testMethods = new LinkedList<>();
                Method beforeEachMethod = null;
                Method afterEachMethod = null;

                for (Method classMethod : testSuite.getDeclaredMethods()) {
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

                Object testSuiteInstance = testSuite.newInstance();
                for (Method testMethod : testMethods) {
                    String testName = testSuite.getName() + "#" + testMethod.getName();
                    try {
                        if (beforeEachMethod != null) beforeEachMethod.invoke(testSuiteInstance);
                        testMethod.invoke(testSuiteInstance);
                        passingTests.add(testName);
                    } catch (InvocationTargetException e) {
                        HashMap<String, String> testNameAndCause = new HashMap<>();
                        testNameAndCause.put(testName, e.getCause().getMessage());
                        failingTests.add(testNameAndCause);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        HashMap<String, String> testNameAndCause = new HashMap<>();
                        testNameAndCause.put(testName, t.getMessage());
                        errorTests.add(testNameAndCause);
                    } finally {
                        if (afterEachMethod != null) afterEachMethod.invoke(testSuiteInstance);
                    }
                }

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("+---------------------------------------------------------------------------------------+");

        }

        if (!passingTests.isEmpty()) {
            System.out.println("Passing tests:");
            passingTests.forEach(test -> System.out.println("\t-" + test));
        }

        if (!failingTests.isEmpty()) {
            System.out.println("Failing tests:");
            failingTests.forEach(testMap -> {
                testMap.forEach((key, value) -> System.out.println("\t-" + key + " failed with cause: " + value));
            });
        }

        if (!errorTests.isEmpty()) {
            System.out.println("Errors:");
            errorTests.forEach(test -> System.out.println("\t-" + test));
        }

    }

    public static List<String> getClassNamesInPackage(String packageName) {

        List<String> classNames = new ArrayList<>();
        File packageDir = new File("target/test-classes/" + packageName.replace('.', '/'));
        File[] packageFiles = packageDir.listFiles();

        StringBuilder pkgBuilder = new StringBuilder(packageName);
        if (packageFiles != null) {
            for (File file : packageFiles) {
                if (file.isDirectory()) {
                    pkgBuilder.append(".").append(file.getName());
                    classNames.addAll(getClassNamesInPackage(packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = pkgBuilder + "." + file.getName().substring(0, file.getName().length() - 6);
                    classNames.add(className);
                }
            }
        }

        return classNames;

    }

}
