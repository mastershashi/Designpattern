import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// User class
class User {
    private String username;
    private List<Document> documents;

    public User(String username) {
        this.username = username;
        this.documents = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}

// Document class
class Document {
    private String name;
    private String content;
    private User owner;
    private List<AccessGrant> accessGrants;

    public Document(String name, String content, User owner) {
        this.name = name;
        this.content = content;
        this.owner = owner;
        this.accessGrants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public List<AccessGrant> getAccessGrants() {
        return accessGrants;
    }
}

// AccessGrant class
class AccessGrant {
    private User user;
    private Permission permission;

    public AccessGrant(User user, Permission permission) {
        this.user = user;
        this.permission = permission;
    }

    public User getUser() {
        return user;
    }

    public Permission getPermission() {
        return permission;
    }
}

// Permission enum
enum Permission {
    READ,
    EDIT
}

// DocumentService class
class DocumentService {
    private Map<String, User> users;
    private Map<String, Document> documents;

    public DocumentService() {
        this.users = new HashMap<>();
        this.documents = new HashMap<>();
    }

    public void createUser(String username) {
        users.put(username, new User(username));
    }

    public void createDocument(String username, String documentName, String content) {
        User user = users.get(username);
        if (user != null) {
            Document document = new Document(documentName, content, user);
            documents.put(documentName, document);
            user.getDocuments().add(document);
        }
    }

    public void grantAccess(String documentName, String username, Permission permission) {
        Document document = documents.get(documentName);
        if (document != null) {
            User user = users.get(username);
            if (user != null) {
                AccessGrant accessGrant = new AccessGrant(user, permission);
                document.getAccessGrants().add(accessGrant);
            }
        }
    }

    public String readDocument(String documentName, String username) {
        Document document = documents.get(documentName);
        if (document != null) {
            User user = users.get(username);
            if (user != null) {
                for (AccessGrant accessGrant : document.getAccessGrants()) {
                    if (accessGrant.getUser().getUsername().equals(username) && accessGrant.getPermission() == Permission.READ) {
                        return document.getContent();
                    }
                }
            }
        }
        return null;
    }

    public void updateDocument(String documentName, String username, String content) {
        Document document = documents.get(documentName);
        if (document != null) {
            User user = users.get(username);
            if (user != null) {
                for (AccessGrant accessGrant : document.getAccessGrants()) {
                    if (accessGrant.getUser().getUsername().equals(username) && accessGrant.getPermission() == Permission.EDIT) {
                        document.setContent(content);
                        break;
                    }
                }
            }
        }
    }

    public void deleteDocument(String documentName, String username) {
        Document document = documents.get(documentName);
        if (document != null) {
            User user = users.get(username);
            if (user != null && user.getUsername().equals(document.getOwner().getUsername())) {
                documents.remove(documentName);
                user.getDocuments().remove(document);
            }
        }
    }
}
public class SimpleDocumentService {
    public static void main(String[] args) {
        DocumentService documentService = new DocumentService();

        documentService.createUser("user1");
        documentService.createUser("user2");

        documentService.createDocument("user1", "document1", "This is document 1.");
        documentService.createDocument("user1", "document2", "This is document 2.");

        documentService.grantAccess("document1", "user2", Permission.READ);
        documentService.grantAccess("document2", "user2", Permission.EDIT);

        System.out.println("Document 1 content: " + documentService.readDocument("document1", "user2"));
        System.out.println("Document 2 content: " + documentService.readDocument("document2", "user2"));

        documentService.updateDocument("document2", "user2", "This is updated document 2.");

        System.out.println("Updated Document 2 content: " + documentService.readDocument("document2", "user2"));

        documentService.deleteDocument("document1", "user1");

        System.out.println("Document 1 deleted: " + (documentService.readDocument("document1", "user2") == null));
    }
}
