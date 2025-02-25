package ru.gpncr.homework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S6204", "java:S112"})
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        initialize();
    }

    private void initialize() {
        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class " + clazz.getName() + " does not have a default constructor", e);
        }

        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = findIdField();
        this.fieldsWithoutId = allFields.stream()
                .filter(field -> !field.equals(idField))
                .collect(Collectors.toList());
    }

    private Field findIdField() {
        return allFields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No field annotated with @Id found in class: " + clazz.getName()));
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
