package coffeeshop;

public class OrderLimitExceededException extends RuntimeException {
    public OrderLimitExceededException(int maxItems) {
        super("You cannot add more than " + maxItems + " items to one order.");
    }
}
