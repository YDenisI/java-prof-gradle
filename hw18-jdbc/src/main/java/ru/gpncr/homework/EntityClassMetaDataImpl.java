package ru.gpncr.homework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S6204", "java:S112"})
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class " + clazz.getName() + " does not have a default constructor", e);
        }
    }

    @Override
    public Field getIdField() {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("No field annotated with @Id found in class: " + clazz.getName()));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.equals(getIdField()))
                .collect(Collectors.toList());
    }
}
