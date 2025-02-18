package ru.gpncr.homework;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import ru.gpncr.core.repository.DataTemplate;
import ru.gpncr.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S3011", "java:S112"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<?> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor
                .executeSelect(connection, sql, List.of(id), resultSet -> {
                    try {
                        if (resultSet.next()) {
                            T instance = (T) entityClassMetaData
                                    .getConstructor()
                                    .newInstance();
                            for (Field field : entityClassMetaData.getAllFields()) {
                                field.setAccessible(true);
                                Object value = resultSet.getObject(field.getName());
                                field.set(instance, value);
                            }
                            return instance;
                        }
                    } catch (SQLException | ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                })
                .map(Optional::of)
                .orElse(Optional.empty());
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor
                .executeSelect(connection, sql, Collections.emptyList(), resultSet -> {
                    List<T> resultList = new ArrayList<>();
                    List<Field> fields = entityClassMetaData.getAllFields();
                    try {
                        while (resultSet.next()) {
                            T instance = (T) entityClassMetaData
                                    .getConstructor()
                                    .newInstance();
                            for (Field field : fields) {
                                field.setAccessible(true);
                                Object value = resultSet.getObject(field.getName());
                                field.set(instance, value);
                            }
                            resultList.add(instance);
                        }
                    } catch (SQLException | ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                    return resultList;
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T client) {
        String sql = entitySQLMetaData.getInsertSql();
        List<Object> params = getValueFields(client);
        return dbExecutor.executeStatement(connection, sql, params);
    }

    @Override
    public void update(Connection connection, T client) {
        String sql = entitySQLMetaData.getUpdateSql();
        List<Object> params = getValueFields(client);
        dbExecutor.executeStatement(connection, sql, params);
    }

    private List<Object> getValueFields(T client) {
        List<Object> params = new ArrayList<>();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(client);
                params.add(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field value", e);
            }
        }
        return params;
    }
}
