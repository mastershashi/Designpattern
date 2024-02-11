package strategy;

public class DiscountFactory {
    public static DiscountContext createDiscountContext(String userType, String category){
        DiscountStrategy calculator;
        // Choose the appropriate calculator based on userType/category
        // Here, simplify by assuming specific rules
        if (userType.equals("regular")) {
            calculator = new RegularDiscount(0.1);
        } else if (userType.equals("premium")) {
            calculator = new PremiumDiscount(10.0);
        } else{
            calculator = new FlatDiscount(0.5);
        }
        return new DiscountContext(userType, category, calculator);
    }
}
