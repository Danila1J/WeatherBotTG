package bot.service;

public class Singleton {
    private static Singleton instance;
    private final Properties properties;

    private Singleton() {
        properties = new Properties();
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public Properties getProperties() {
        return properties;
    }
}
