
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Column class
class Column {
    private String name;
    private String type;
    private Constraint constraint;

    public Column(String name, String type, Constraint constraint) {
        this.name = name;
        this.type = type;
        this.constraint = constraint;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Constraint getConstraint() {
        return constraint;
    }
}

// Constraint class
class Constraint {
    private String type;
    private Object value;

    public Constraint(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}

// Row class
class Row {
    private Map<String, Object> values;

    public Row(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}

// Table class
class Table {
    private String name;
    private List<Column> columns;
    private List<Row> rows;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
        this.rows = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void addRow(Row row) {
        rows.add(row);
    }
}

// Database class
class Database {
    private Map<String, Table> tables;

    public Database() {
        this.tables = new HashMap<>();
    }

    public void createTable(String tableName, List<Column> columns) {
        Table table = new Table(tableName, columns);
        tables.put(tableName, table);
    }

    public void insertRow(String tableName, Row row) {
        Table table = tables.get(tableName);
        if (table != null) {
            table.addRow(row);
        }
    }

    public void printTable(String tableName) {
        Table table = tables.get(tableName);
        if (table != null) {
            System.out.println("Table: " + table.getName());
            for (Column column : table.getColumns()) {
                System.out.print(column.getName() + " ");
            }
            System.out.println();
            for (Row row : table.getRows()) {
                for (Object value : row.getValues().values()) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }
    }

    public void filterTable(String tableName, String columnName, Object value) {
        Table table = tables.get(tableName);
        if (table != null) {
            System.out.println("Filtered Table: " + table.getName());
            for (Column column : table.getColumns()) {
                System.out.print(column.getName() + " ");
            }
            System.out.println();
            for (Row row : table.getRows()) {
                if (row.getValues().get(columnName).equals(value)) {
                    for (Object val : row.getValues().values()) {
                        System.out.print(val + " ");
                    }
                    System.out.println();
                }
            }
        }
    }
}

public class InMemoryDatabase {
    public static void main(String[] args) {
        Database database = new Database();

        // Create table
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("id", "int", new Constraint("min", 1)));
        columns.add(new Column("name", "string", new Constraint("max_length", 20)));
        database.createTable("users", columns);

        // Insert rows
        Map<String, Object> values1 = new HashMap<>();
        values1.put("id", 1);
        values1.put("name", "John Doe");
        database.insertRow("users", new Row(values1));

        Map<String, Object> values2 = new HashMap<>();
        values2.put("id", 2);
        values2.put("name", "Jane Doe");
        database.insertRow("users", new Row(values2));

        // Print table
        database.printTable("users");

        // Filter table
        database.filterTable("users", "name", "John Doe");
    }
}
