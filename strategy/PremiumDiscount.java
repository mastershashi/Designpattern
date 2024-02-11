package strategy;

public class PremiumDiscount implements DiscountStrategy{
    private double amount;

    public PremiumDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculateDiscount(double price) {
        return Math.min(amount, price);
    }
}
