package ru.gpncr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"java:S2629", "java:S3457"})
public class LoggingProxy implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggingProxy.class);
    private final Object target;
    private final Map<String, Method> loggableMethods = new HashMap<>();

    public LoggingProxy(Object target) {
        this.target = target;
        loggableMethods(target);
    }

    private void loggableMethods(Object target) {
        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Log.class)) {
                String key = method.getName() + Arrays.toString(method.getParameterTypes());
                loggableMethods.put(key, method);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String key = method.getName() + Arrays.toString(method.getParameterTypes());
        Method originalMethod = loggableMethods.get(key);
        if (originalMethod != null) {
            log.info("executed method: {}, param: {}", method.getName(), Arrays.toString(args));
        }
        return method.invoke(target, args);
    }

    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(), target.getClass().getInterfaces(), new LoggingProxy(target));
    }
}
