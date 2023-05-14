package Util;


import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Util {

    public static String loadClassPathProperty( String filename, String property) {

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream( filename);

        Properties properties;
        try {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(property);
    }

    public static String loadPayloadFromClassPathFile(String filename) throws IOException, URISyntaxException {
        File file = new File(ClassLoader.getSystemClassLoader().getResource(filename).toURI());
        return FileUtils.readFileToString(file, "UTF-8");
    }




}
