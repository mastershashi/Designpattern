package strategy;

class ShoppingCart {
// consumption class for strategy pattern
    private DiscountContext discountContext;

    public ShoppingCart(String userType, String category) {
        this.discountContext = DiscountFactory.createDiscountContext(userType, category);
    }

    public double calculateFinalPrice(double price) {
        double discount = discountContext.calculateDiscount(price);
        return price - discount;
    }
}
