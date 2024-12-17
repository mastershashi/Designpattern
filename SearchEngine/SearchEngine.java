import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Document class
class Document {
    private int id;
    private String text;
    private Set<String> keywords;
    private int size;

    public Document(int id, String text) {
        this.id = id;
        this.text = text;
        this.keywords = tokenizeText(text);
        this.size = text.length();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public int getSize(){
        return this.size;
    }

    private Set<String> tokenizeText(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}

// Observer interface
interface DocumentObserver {
    void onDocumentAdded(Document document);
    void onDocumentRemoved(Document document);
}

// SearchIndex class
class SearchIndex implements DocumentObserver {
    private Map<String, Set<Integer>> keywordToDocumentIds;
    private RankingStrategy rankingStrategy;

    public SearchIndex(RankingStrategy rankingStrategy) {
        this.keywordToDocumentIds = new HashMap<>();
        this.rankingStrategy = rankingStrategy;
    }

    @Override
    public void onDocumentAdded(Document document) {
        addDocument(document);
    }

    @Override
    public void onDocumentRemoved(Document document) {
        removeDocument(document);
    }

    private void addDocument(Document document) {
        document.getKeywords().forEach(keyword -> {
            keywordToDocumentIds.computeIfAbsent(keyword, k -> new HashSet<>()).add(document.getId());
        });
    }

    private void removeDocument(Document document) {
        document.getKeywords().forEach(keyword -> {
            keywordToDocumentIds.computeIfAbsent(keyword, k -> new HashSet<>()).remove(document.getId());
        });
    }

    public List<Document> search(String query) {
        Set<Integer> documentIds = keywordToDocumentIds.entrySet().stream()
                .filter(entry -> query.contains(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());

        return rankingStrategy.rankDocuments(documentIds.stream()
                .map(DocumentRegistry::getDocument)
                .collect(Collectors.toList()), query);
    }
}

// RankingStrategy interface
interface RankingStrategy {
    List<Document> rankDocuments(List<Document> documents, String query);
}

// RankByRelevance strategy
class RankByRelevance implements RankingStrategy {
    @Override
    public List<Document> rankDocuments(List<Document> documents, String query) {
        return documents.stream()
                .sorted(Comparator.comparingInt(doc -> getRelevanceScore(doc, query)))
                .collect(Collectors.toList());
    }

    private int getRelevanceScore(Document document, String query) {
        return (int) document.getKeywords().stream()
                .filter(keyword -> query.contains(keyword))
                .count();
    }
}
// RankByDocumentSize strategy
class RankByDocumentSize implements RankingStrategy {
    @Override
    public List<Document> rankDocuments(List<Document> documents, String query) {
        // Rank documents by size
        return documents.stream()
                .sorted(Comparator.comparingInt(Document::getSize))
                .collect(Collectors.toList());
    }
}

// DocumentRegistry class
class DocumentRegistry {
    private static Map<Integer, Document> documents = new HashMap<>();
    private List<DocumentObserver> observers = new ArrayList<>();

    public void registerObserver(DocumentObserver observer) {
        observers.add(observer);
    }

    public void registerDocument(Document document) {
        documents.put(document.getId(), document);
        observers.forEach(observer -> observer.onDocumentAdded(document));
    }

    public void removeDocument(Document document) {
        documents.remove(document.getId());
        observers.forEach(observer -> observer.onDocumentRemoved(document));
    }

    public static Document getDocument(int id) {
        return documents.get(id);
    }
}

public class SearchEngine {
    public static void main(String[] args) {
        // Create a document registry
        DocumentRegistry registry = new DocumentRegistry();

        // Create a search index with a ranking strategy
        SearchIndex searchIndex = new SearchIndex(new RankByRelevance());
        registry.registerObserver(searchIndex);

        // Register documents
        registry.registerDocument(new Document(1, "This is a sample document"));
        registry.registerDocument(new Document(2, "Another sample document"));

        // Search for documents
        List<Document> results = searchIndex.search("sample");
        results.forEach(document -> System.out.println("Document ID: " + document.getId() + ", Text: " + document.getText()));
    }
}