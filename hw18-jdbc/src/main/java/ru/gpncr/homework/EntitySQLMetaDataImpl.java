package ru.gpncr.homework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;
    private final Map<String, String> sqlCache = new HashMap<>();

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return sqlCache.computeIfAbsent("selectAll", key -> {
            String tableName = entityClassMetaData.getName().toLowerCase();
            return "SELECT * FROM " + tableName;
        });
    }

    @Override
    public String getSelectByIdSql() {
        return sqlCache.computeIfAbsent("selectById", key -> {
            String tableName = entityClassMetaData.getName().toLowerCase();
            String idFieldName = entityClassMetaData.getIdField().getName();
            return "SELECT * FROM " + tableName + " WHERE " + idFieldName + " = ?";
        });
    }

    @Override
    public String getInsertSql() {
        return sqlCache.computeIfAbsent("insert", key -> {
            String tableName = entityClassMetaData.getName().toLowerCase();
            List<Field> fieldNames = entityClassMetaData.getFieldsWithoutId();
            String columns = fieldNames.stream().map(Field::getName).collect(Collectors.joining(", "));
            String placeholders = fieldNames.stream().map(field -> "?").collect(Collectors.joining(", "));
            return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        });
    }

    @Override
    public String getUpdateSql() {
        return sqlCache.computeIfAbsent("update", key -> {
            String tableName = entityClassMetaData.getName().toLowerCase();
            String idFieldName = entityClassMetaData.getIdField().getName();
            List<Field> fieldNames = entityClassMetaData.getAllFields();
            String setClause = fieldNames.stream()
                    .map(field -> field.getName() + " = ?")
                    .collect(Collectors.joining(", "));
            return "UPDATE " + tableName + " SET " + setClause + " WHERE " + idFieldName + " = ?";
        });
    }
}
