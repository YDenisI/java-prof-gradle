package ru.gpncr;

public class Demo {
    public void action() {
        TestLoggingInterface logging = LoggingProxy.createProxy(new TestLogging());
        logging.calculation(6);
        logging.calculation(3, 4);
        logging.calculation(5, 10, "test");
    }

    public static void main(String[] args) {
        new Demo().action();
    }
}
