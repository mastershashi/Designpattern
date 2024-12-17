import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Document class to represent each document
class Document {
    int id;
    String content;

    public Document(int id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Document{id=" + id + ", content='" + content + "'}";
    }
}

// Optimized InvertedIndex class
class InvertedIndex {
    private Map<String, Set<Integer>> index;

    public InvertedIndex() {
        this.index = new ConcurrentHashMap<>(); // Use ConcurrentHashMap for thread safety in multi-threaded environment
    }

    // Optimized method to build index in parallel using parallel streams
    public void buildIndex(List<Document> documents) {
        // Using parallel stream to process documents concurrently
        documents.parallelStream().forEach(doc -> {
            String[] words = doc.content.split("\\s+");
            Set<String> uniqueWords = new HashSet<>();

            // Tokenize and normalize words in the document
            for (String word : words) {
                word = word.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""); // Normalize word (lowercase + alphanumeric)
                if (!word.isEmpty() && !uniqueWords.contains(word)) {
                    uniqueWords.add(word);
                    index.computeIfAbsent(word, k -> ConcurrentHashMap.newKeySet()).add(doc.id); // Add document id for the word
                }
            }
        });
    }

    // Optimized keyword matching
    public Set<Integer> matchKeyword(String keyword) {
        keyword = keyword.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""); // Normalize keyword
        return index.getOrDefault(keyword, new HashSet<>()); // Return matching document IDs
    }

    // Printing the inverted index
    public void printIndex() {
        index.forEach((keyword, docIds) -> {
            System.out.println("Keyword: " + keyword + " -> Documents: " + docIds);
        });
    }
}

public class InvertedIndexDemo {
    public static void main(String[] args) {
        // Sample documents
        List<Document> documents = Arrays.asList(
            new Document(1, "Java is a programming language"),
            new Document(2, "Java programming is fun"),
            new Document(3, "Python is a programming language")
        );

        // Create and build the inverted index in parallel
        InvertedIndex invertedIndex = new InvertedIndex();
        long startTime = System.nanoTime();
        invertedIndex.buildIndex(documents);
        long endTime = System.nanoTime();

        // Print the inverted index
        System.out.println("Inverted Index:");
        invertedIndex.printIndex();

        // Calculate the time taken for building the index
        System.out.println("\nTime taken to build index: " + (endTime - startTime) / 1_000_000.0 + " ms");

        // Keyword matching example
        String keyword = "java";
        Set<Integer> matchingDocs = invertedIndex.matchKeyword(keyword);

        // Display the documents containing the keyword
        System.out.println("\nDocuments containing the keyword \"" + keyword + "\":");
        if (!matchingDocs.isEmpty()) {
            for (Integer docId : matchingDocs) {
                System.out.println("Document ID: " + docId);
            }
        } else {
            System.out.println("No documents found for the keyword.");
        }
    }
}
