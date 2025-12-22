package com.next.engine.io;

import java.io.InputStream;
import java.net.URL;

public class FileReader {

    public static InputStream getFile(String path) {
        return FileReader.class.getResourceAsStream(path);
    }

    public static URL getResource(String path) {
        return FileReader.class.getResource(path);
    }
}
