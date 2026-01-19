package com.next.engine.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonReader {
    private JsonReader() {}

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readObject(InputStream stream, Class<T> clazz) throws IOException {
        return mapper.readValue(stream, clazz);
    }

    public static <T, U> Map<T, U> readMap(InputStream stream, Class<T> key, Class<U> value) throws IOException {
        return mapper.readValue(stream, mapper.getTypeFactory().constructMapType(HashMap.class, key, value));
    }

    public static <T> List<T> readList(InputStream stream, Class<T> clazz) throws IOException {
        return mapper.readValue(stream, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
