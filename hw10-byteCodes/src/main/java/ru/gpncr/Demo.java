package ru.gpncr;

public class Demo {
    public void action() {
        TestLoggingInterface testLogging = LoggingProxy.createTestClass();
        testLogging.calculation(6);
        testLogging.calculation(3, 4);
        testLogging.calculation(1, 2, "test");

        new TestLogging().calculation(22);
    }

    public static void main(String[] args) {
        new Demo().action();
    }
}
