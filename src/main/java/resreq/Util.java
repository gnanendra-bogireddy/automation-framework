package resreq;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {

    public static String loadClassPathProperty(String filename, String property) {

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);

        Properties properties;
        try {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(property);
    }




}
