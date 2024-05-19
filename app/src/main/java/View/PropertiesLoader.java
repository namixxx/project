/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
/**
 *
 * @author Zyron
 */
public class PropertiesLoader {
    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("flatlaf.properties")) {
            if (input == null) {
                throw new IllegalStateException("flatlaf.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
