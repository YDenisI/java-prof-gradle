package ru.gpncr.homework;

import java.lang.reflect.Method;

@SuppressWarnings({"java:S106", "java:S1104", "java:S1144", "java:S112", "java:S1141"})
public class TestRunner {

    private TestRunner() {}

    public static void runTests(String className) {
        try {
            Class<?> cls = Class.forName(className);
            Method[] methods = cls.getDeclaredMethods();

            int totalTests = 0;
            int successfulTests = 0;
            int failedTests = 0;

            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class)) {
                    totalTests++;
                    Object instance = cls.getDeclaredConstructor().newInstance();
                    try {
                        invokeBeforeMethods(methods, instance);
                        method.invoke(instance);
                        successfulTests++;
                    } catch (Exception e) {
                        failedTests++;
                        System.err.println("Test failed: " + method.getName() + " - " + e.getCause());
                    }
                    invokeAfterMethods(methods, instance);
                }
            }

            System.out.println("Total tests: " + totalTests);
            System.out.println("Successful tests: " + successfulTests);
            System.out.println("Failed tests: " + failedTests);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void invokeBeforeMethods(Method[] methods, Object instance) throws Exception {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                method.invoke(instance);
            }
        }
    }

    private static void invokeAfterMethods(Method[] methods, Object instance) throws Exception {
        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                method.invoke(instance);
            }
        }
    }
}
