package strategy;

public class DiscountContext {
    private DiscountStrategy calculator;
    private String userType;
    private String category;
    public DiscountContext(String userType, String category, DiscountStrategy calculator) {
        this.userType = userType;
        this.category = category;
        this.calculator = calculator;
    }
    public double calculateDiscount(double price) {
        // Apply any additional logic based on userType/category, then delegate to calculator
        return calculator.calculateDiscount(price);
    }
}
