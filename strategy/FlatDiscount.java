package strategy;

public class FlatDiscount implements DiscountStrategy{
    private double amount;

    public FlatDiscount(double amount) {
        this.amount = amount;
    }
    @Override
    public double calculateDiscount(double price) {
        return Math.min(amount, price);
    }
}
