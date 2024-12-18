import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Client class
class Client {
    private String id;
    private String name;

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// PaymentGateway class
class PaymentGateway {
    private String id;
    private String name;
    private List<Bank> banks;
    private List<PaymentMethod> paymentMethods;
    private Router router;

    public PaymentGateway(String id, String name) {
        this.id = id;
        this.name = name;
        this.banks = new ArrayList<>();
        this.paymentMethods = new ArrayList<>();
        this.router = new Router();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public Router getRouter() {
        return router;
    }

    public void addBank(Bank bank) {
        banks.add(bank);
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }
}

// Bank class
class Bank {
    private String id;
    private String name;

    public Bank(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// PaymentMethod class
class PaymentMethod {
    private String id;
    private String name;

    public PaymentMethod(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// Router class
class Router {
    private Map<String, Bank> bankMap;

    public Router() {
        this.bankMap = new HashMap<>();
    }

    public void addBank(String paymentMethodId, Bank bank) {
        bankMap.put(paymentMethodId, bank);
    }

    public Bank getBank(String paymentMethodId) {
        return bankMap.get(paymentMethodId);
    }
}

// Transaction class
class Transaction {
    private String id;
    private double amount;
    private PaymentMethod paymentMethod;
    private Bank bank;

    public Transaction(String id, double amount, PaymentMethod paymentMethod, Bank bank) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.bank = bank;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Bank getBank() {
        return bank;
    }
}

// PaymentGatewaySystem class
public class PaymentGatewaySystem {
public static void main(String[] args) {
// Create clients
Client client1 = new Client("1", "Flipkart");
Client client2 = new Client("2", "Amazon");

    // Create payment gateways
    PaymentGateway paymentGateway1 = new PaymentGateway("1", "Razorpay");
    PaymentGateway paymentGateway2 = new PaymentGateway("2", "PayPal");

    // Create banks
    Bank bank1 = new Bank("1", "HDFC");
    Bank bank2 = new Bank("2", "ICICI");

    // Create payment methods
    PaymentMethod paymentMethod1 = new PaymentMethod("1", "Credit Card");
    PaymentMethod paymentMethod2 = new PaymentMethod("2", "Net Banking");

    // Add banks and payment methods to payment gateways
    paymentGateway1.addBank(bank1);
    paymentGateway1.addPaymentMethod(paymentMethod1);
    paymentGateway2.addBank(bank2);
    paymentGateway2.addPaymentMethod(paymentMethod2);

    // Create transactions
    Transaction transaction1 = new Transaction("1", 100.0, paymentMethod1, bank1);
    Transaction transaction2 = new Transaction("2", 200.0, paymentMethod2, bank2);

    // Process transactions
    paymentGateway1.getRouter().addBank(paymentMethod1.getId(), bank1);
    paymentGateway2.getRouter().addBank(paymentMethod2.getId(), bank2);

    Bank bank = paymentGateway1.getRouter().getBank(paymentMethod1.getId());
    System.out.println("Transaction 1 processed through bank: " + bank.getName());

    bank = paymentGateway2.getRouter().getBank(paymentMethod2.getId());
    System.out.println("Transaction 2 processed through bank: " + bank.getName());
}

}
