import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Log class
class Log {
    private int id;
    private String timestamp;
    private String logLevel;
    private String message;
    private String system;
    private String landscape;

    public Log(int id, String timestamp, String logLevel, String message, String system, String landscape) {
        this.id = id;
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
        this.system = system;
        this.landscape = landscape;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    public String getSystem() {
        return system;
    }

    public String getLandscape() {
        return landscape;
    }
}

// Logger class
class Logger {
    private int id;
    private String name;
    private String logLevel;

    public Logger(int id, String name, String logLevel) {
        this.id = id;
        this.name = name;
        this.logLevel = logLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogLevel() {
        return logLevel;
    }
}

// LogFilter class
class LogFilter {
    private int id;
    private String name;
    private String filterCriteria;

    public LogFilter(int id, String name, String filterCriteria) {
        this.id = id;
        this.name = name;
        this.filterCriteria = filterCriteria;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilterCriteria() {
        return filterCriteria;
    }
}

// LogAggregator class
class LogAggregator {
    private int id;
    private String name;
    private String aggregationCriteria;

    public LogAggregator(int id, String name, String aggregationCriteria) {
        this.id = id;
        this.name = name;
        this.aggregationCriteria = aggregationCriteria;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAggregationCriteria() {
        return aggregationCriteria;
    }
}

// BloomFilter class
class BloomFilter {
    private int size;
    private boolean[] bits;

    public BloomFilter(int size) {
        this.size = size;
        this.bits = new boolean[size];
    }

    public void add(String element) {
        int hash1 = hash(element, 1);
        int hash2 = hash(element, 2);
        bits[hash1 % size] = true;
        bits[hash2 % size] = true;
    }

    public boolean contains(String element) {
        int hash1 = hash(element, 1);
        int hash2 = hash(element, 2);
        return bits[hash1 % size] && bits[hash2 % size];
    }

    private int hash(String element, int seed) {
        int hash = seed;
        for (char c : element.toCharArray()) {
            hash = (hash * 31 + c) % size;
        }
        return hash;
    }
}

// MapReduceLogAggregator class
// MapReduceLogAggregator class
class MapReduceLogAggregator {
    public List<Log> aggregateLogs(List<Log> logs) {
        // Map phase
        Map<String, List<Log>> mappedLogs = logs.stream()
                .collect(Collectors.groupingBy(Log::getSystem));

        // Reduce phase
        List<Log> aggregatedLogs = new ArrayList<>();
        for (Map.Entry<String, List<Log>> entry : mappedLogs.entrySet()) {
            String aggregatedMessage = entry.getValue().stream()
                    .map(Log::getMessage)
                    .collect(Collectors.joining(","));
            Log aggregatedLog = new Log(0, "", "", aggregatedMessage, entry.getKey(), "");
            aggregatedLogs.add(aggregatedLog);
        }

        return aggregatedLogs;
    }
}

// LoggerService class
class LoggerService {
    private Map<Integer, Logger> loggers;
    private Map<Integer, LogFilter> logFilters;
    private Map<Integer, LogAggregator> logAggregators;
    private BloomFilter bloomFilter;
    private MapReduceLogAggregator mapReduceLogAggregator;
    private List<Log> filteredAndAggregatedLogs;

    public LoggerService() {
        this.loggers = new HashMap<>();
        this.logFilters = new HashMap<>();
        this.logAggregators = new HashMap<>();
        this.bloomFilter = new BloomFilter(1000);
        this.mapReduceLogAggregator = new MapReduceLogAggregator();
        this.filteredAndAggregatedLogs = new ArrayList<>();
    }

    public void addLogger(Logger logger) {
        loggers.put(logger.getId(), logger);
    }

    public void addLogFilter(LogFilter logFilter) {
        logFilters.put(logFilter.getId(), logFilter);
    }

    public void addLogAggregator(LogAggregator logAggregator) {
        logAggregators.put(logAggregator.getId(), logAggregator);
    }

    public void log(Log log) {
        System.out.println("Logging: " + log.getMessage());
    
        // Check if log is already present in the bloom filter
        if (!bloomFilter.contains(log.getMessage())) {
            System.out.println("Log is not present in the bloom filter. Adding it.");
    
            // Add log to the bloom filter
            bloomFilter.add(log.getMessage());
    
            // Filter log using log filters
            List<LogFilter> applicableFilters = logFilters.values().stream()
                    .filter(filter -> filter.getFilterCriteria().equals(log.getLogLevel()))
                    .collect(Collectors.toList());
    
            if (applicableFilters.isEmpty()) {
                System.out.println("No applicable filters found. Proceeding with aggregation.");
    
                // Aggregate log using log aggregators
                List<LogAggregator> applicableAggregators = logAggregators.values().stream()
                        .filter(aggregator -> aggregator.getAggregationCriteria().equals(log.getSystem()))
                        .collect(Collectors.toList());
    
                if (applicableAggregators.isEmpty()) {
                    System.out.println("No applicable aggregators found. Storing log in the database.");
    
                    // Store log in the database
                    System.out.println("Storing log in the database: " + log.getMessage());
                    filteredAndAggregatedLogs.add(log);
                } else {
                    System.out.println("Applicable aggregators found. Aggregating logs using MapReduce.");
    
                    // Aggregate log using MapReduce
                    List<Log> aggregatedLogs = mapReduceLogAggregator.aggregateLogs(Collections.singletonList(log));
                    System.out.println("Aggregated log: " + aggregatedLogs.get(0).getMessage());
                    filteredAndAggregatedLogs.addAll(aggregatedLogs);
                }
            } else {
                System.out.println("Applicable filters found. Adding log to filtered and aggregated logs.");
    
                // Add log to filtered and aggregated logs
                filteredAndAggregatedLogs.add(log);
            }
        } else {
            System.out.println("Log is already present in the bloom filter. Skipping it.");
        }
    }
    public List<Log> getFilteredAndAggregatedLogs() {
        return filteredAndAggregatedLogs;
    }
}
public class LoggerSystem {
    public static void main(String[] args) {
        LoggerService loggerService = new LoggerService();

        // Create loggers
        Logger logger1 = new Logger(1, "Logger 1", "INFO");
        loggerService.addLogger(logger1);

        // Create log filters
        LogFilter logFilter1 = new LogFilter(1, "Filter 1", "INFO");
        loggerService.addLogFilter(logFilter1);

        // Create log aggregators
        LogAggregator logAggregator1 = new LogAggregator(1, "Aggregator 1", "System 1");
        loggerService.addLogAggregator(logAggregator1);

        // Log messages
        Log log1 = new Log(1, "2022-01-01 12:00:00", "INFO", "This is a log message.", "System 1", "Landscape 1");
        loggerService.log(log1);

        Log log2 = new Log(2, "2022-01-01 12:00:01", "INFO", "This is another log message.", "System 1", "Landscape 1");
        loggerService.log(log2);

        System.out.println("Filtered and aggregated logs:");
        loggerService.getFilteredAndAggregatedLogs().stream().filter(log -> log.getId() == 1).forEach( log -> System.out.println(log.getMessage()));
    }
}

