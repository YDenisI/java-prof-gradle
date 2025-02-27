package ru.gpncr.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import ru.gpncr.appcontainer.api.AppComponent;
import ru.gpncr.appcontainer.api.AppComponentsContainer;
import ru.gpncr.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings({"java:S112"})
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Method[] methods = configClass.getDeclaredMethods();

        Arrays.sort(methods, Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class)
                .order()));

        for (Method method : methods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                try {

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];

                    for (int i = 0; i < parameterTypes.length; i++) {
                        parameters[i] = getAppComponent(parameterTypes[i]);
                    }

                    Object component =
                            method.invoke(configClass.getDeclaredConstructor().newInstance(), parameters);
                    appComponents.add(component);

                    String componentName =
                            method.getAnnotation(AppComponent.class).name();

                    if (appComponentsByName.containsKey(componentName)) {
                        throw new IllegalArgumentException("Duplicate component name: " + componentName);
                    }
                    appComponentsByName.put(componentName, component);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {

        List<C> foundComponents = new ArrayList<>();

        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                foundComponents.add((C) component);
            }
        }

        if (foundComponents.isEmpty()) {
            throw new RuntimeException("Component not found: " + componentClass.getName());
        }

        if (foundComponents.size() > 1) {
            throw new RuntimeException("Duplicate components found for class: " + componentClass.getName());
        }

        return foundComponents.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException("Component not found" + componentName);
        }
        return component;
    }
}
