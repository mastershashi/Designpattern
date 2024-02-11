package singleton;

public class Singleton {

    private static volatile Singleton instance = null;

    private Singleton() {} // Private constructor to prevent external instantiation

    // Thread-safe, lazy initialization using double-checked locking
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    // Example method within the singleton
    public void doSomething() {
        // Add functionality here
    }
}

