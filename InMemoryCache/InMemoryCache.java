import java.util.HashMap;
import java.util.Map;

// Cache interface
interface Cache {
    void put(String key, String value);
    String get(String key);
}

// LRU Cache class
class LRUCache implements Cache {
    private final int capacity;
    private final Map<String, Node> cache;
    private final Node head;
    private final Node tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void put(String key, String value) {
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            remove(node);
            add(node);
        } else {
            if (cache.size() >= capacity) {
                cache.remove(tail.prev.key);
                remove(tail.prev);
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            add(newNode);
        }
    }

    @Override
    public String get(String key) {
        Node node = cache.get(key);
        if (node != null) {
            remove(node);
            add(node);
            return node.value;
        }
        return null;
    }

    private void add(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private class Node {
        String key;
        String value;
        Node prev;
        Node next;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}

// Cache Factory class
class CacheFactory {
    public static Cache createCache(int capacity) {
        return new LRUCache(capacity);
    }
}

// Observer interface
interface Observer {
    void update(String key, String value);
}

// Cache Observer class
class CacheObserver implements Observer {
    @Override
    public void update(String key, String value) {
        System.out.println("Cache updated: key=" + key + ", value=" + value);
    }
}

// Strategy interface
interface Strategy {
    void evict(Cache cache);
}

// LRU Strategy class
class LRUStrategy implements Strategy {
    @Override
    public void evict(Cache cache) {
        // LRU eviction logic
    }
}

// Cache Manager class
class CacheManager {
    private Cache cache;
    private Observer observer;
    private Strategy strategy;

    public CacheManager(Cache cache, Observer observer, Strategy strategy) {
        this.cache = cache;
        this.observer = observer;
        this.strategy = strategy;
    }

    public void put(String key, String value) {
        cache.put(key, value);
        observer.update(key, value);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void evict() {
        strategy.evict(cache);
    }
}

public class InMemoryCache {
    public static void main(String[] args) {
        Cache cache = CacheFactory.createCache(5);
        Observer observer = new CacheObserver();
        Strategy strategy = new LRUStrategy();
        CacheManager cacheManager = new CacheManager(cache, observer, strategy);

        cacheManager.put("key1", "value1");
        cacheManager.put("key2", "value2");
        cacheManager.put("key3", "value3");
        cacheManager.put("key4", "value4");
        cacheManager.put("key5", "value5");

        System.out.println(cacheManager.get("key1"));
        System.out.println(cacheManager.get("key2"));

        cacheManager.evict();
    }
}
