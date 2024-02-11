package strategy;

public class RegularDiscount implements DiscountStrategy{
    private double percentage;

    public RegularDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(double price) {
        return price * percentage;
    }
}
