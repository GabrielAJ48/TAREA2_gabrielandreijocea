package utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Propiedades {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = Propiedades.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("No se encontró el archivo application.properties");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Propiedades() { }

    public static String get(String key) {
        return props.getProperty(key);
    }
}