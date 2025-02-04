package ru.gpncr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"java:S2629"})
public class LoggingProxy {
    private static final Logger log = LoggerFactory.getLogger(LoggingProxy.class);

    private LoggingProxy() {}

    static TestLoggingInterface createTestClass() {
        InvocationHandler handler = new TestInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                LoggingProxy.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class TestInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testClass;

        TestInvocationHandler(TestLoggingInterface testClass) {
            this.testClass = testClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method originalMethod = testClass.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (originalMethod.isAnnotationPresent(Log.class)) {
                log.info("executed method: {}, param: {}", method.getName(), java.util.Arrays.toString(args));
            }
            return method.invoke(testClass, args);
        }
    }
}
