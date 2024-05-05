package com.tutorial.cdc.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@UtilityClass
public class HandlerUtils {
    /**
     * Extracts the document ID from the given Struct object.
     *
     * @param key The Struct object containing the document information.
     * @return The extracted document ID, or null if not found.
     */
    public static Long getDocumentId(Struct key) {
        return key.getInt64("id");
    }

    /**
     * Extracts the collection name from the source record value.
     *
     * @param sourceRecordValue The Struct object representing the source record.
     * @return The name of the collection.
     */
    public static String getTable(Struct sourceRecordValue) {
        Struct source = (Struct) sourceRecordValue.get("source");
        return source.getString("table");
    }

    /**
     * Deserializes the 'after' field of the source record value into a Product object.
     *
     * @param sourceRecordValue The Struct object representing the source record.
     * @return The deserialized Product object.
     * @throws IOException If there is an error during deserialization.
     */
    public static Map<String, Object> getData(Struct sourceRecordValue) throws IOException {
        var source = sourceRecordValue.getStruct("after");

        return source.schema().fields().stream()
             .map(Field::name)
             .filter(fieldName -> source.get(fieldName) != null)
             .map(fieldName -> Pair.of(fieldName, source.get(fieldName)))
             .collect(toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * Retrieves the operation type from the source record value.
     *
     * @param sourceRecordValue The Struct object representing the source record.
     * @return The operation type, such as "insert", "update", or "delete".
     */
    public static String getOperation(Struct sourceRecordValue) {
        return sourceRecordValue.getString("op");
    }
}
