import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyWordSearch {
 private static int k =1;
 private static Map<String, Set<Integer>> invertedIndexMap = new ConcurrentHashMap<>();
    static class Document{
        Integer documentId;
        String content;
        public Document(int id, String content) {
            this.documentId = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Document{id=" + documentId + ", content='" + content + "'}";
        }
    }
    

    public static void createInvertedIndex(List<Document> documents){
       
        documents.parallelStream().forEach(document ->{
            String[] words = document.content.split("\\s+");
            for(String word: words){
                word = word.toLowerCase().replaceAll("^a-z0-9","");
                if(!word.isEmpty()){
                 invertedIndexMap.computeIfAbsent(word, k->ConcurrentHashMap.newKeySet()).add(document.documentId);
                }
            }
        });
    }

    public static List<Document> readDocumentsFromFile(String fileName) {
        List<Document> documents = new ArrayList<>();
        try{
                File file = new File(fileName);
                if(file.exists() && file.isFile()){
                    String content = new String(Files.readAllBytes(file.toPath()));
                    documents.add(new Document(k,content));
                }
        }catch(Exception e){
                System.out.println(e.getMessage());
            return null;
        }
        return documents;
    }

    public static Set<Integer> searchKeyword(String keyword){
        keyword = keyword.toLowerCase().replaceAll("[^a-z0-9]", ""); // Normalize keyword
        return invertedIndexMap.getOrDefault(keyword, new HashSet<>()); // Return matching document IDs
    }
    public static void main(String args[]){
        List<Document> documents = new ArrayList<>();
        List<Document> document1 = readDocumentsFromFile("document1.txt");
        List<Document> document2 = readDocumentsFromFile("document2.txt");
        List<Document> document3 = readDocumentsFromFile("document3.txt");
        documents.addAll(document1);
        documents.addAll(document2);
        documents.addAll(document3);
        createInvertedIndex(documents);
        Set<Integer> resultedDocuments =  searchKeyword("java");
        resultedDocuments.forEach(doc -> System.out.println("java found in document " + doc));

    }
}
