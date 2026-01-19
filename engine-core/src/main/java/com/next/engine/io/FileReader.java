package com.next.engine.io;

import java.io.InputStream;
import java.net.URL;

public final class FileReader {
    private FileReader() {}

    public static InputStream getFile(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(getPath(path));
    }

    public static URL getResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResource(getPath(path));
    }

    private static String getPath(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }
}
