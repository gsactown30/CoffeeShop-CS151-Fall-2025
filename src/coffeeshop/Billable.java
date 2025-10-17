package coffeeshop;

public interface Billable {

    double calculateTotal(int qty);

    String getItemName();

    double getUnitPrice();

    String getDescription();

    boolean isAvailable();

    double roundPrice();

}


