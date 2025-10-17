package coffeeshop;

// menu item for the coffee shop to hold info, prices, etc
public class MenuItem implements Billable {
    private static int instanceCount = 0;
    public static final int MAX_MENU_ITEMS = 100;

    private String name;
    private String description;
    private double price;
    private boolean available = true;

    // makes sure price/name/desc are valid before saving
    public MenuItem(String name, String description, double price) {
        // stop here if cap hit
        if (instanceCount >= MAX_MENU_ITEMS) {
            throw new InvalidOrderException("menu capacity reached (max " + MAX_MENU_ITEMS + ")");
        }
        setName(name);
        setDescription(description);
        setPrice(price);
        instanceCount++; // track how many we made
    }

    // extra constructor so CoffeeShop can make menu items with just name + price
    public MenuItem(String name, double price) {
        this(name, name, price);
    }


    //setters with quick checks

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidOrderException("name can’t be empty");
        }
        this.name = name.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidOrderException("description can’t be empty");
        }
        this.description = description.trim();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidOrderException("price can’t be negative");
        }
        this.price = roundPrice(price); // round to cents
    }

    // toggle sold out flag
    public void setAvailability(boolean flag) {
        this.available = flag;
    }

    // apply percent off discount, and throws if bad value
    public double applyDiscount(double percent) {
        if (percent < 0 || percent > 100) {
            throw new InvalidOrderException("discount must be 0..100");
        }
        double discounted = price * (1 - percent / 100.0);
        return roundPrice(discounted); // round to cents
    }

    // price math with quantity + availability check
    @Override
    public double calculateTotal(int qty) {
        if (qty <= 0) {
            throw new InvalidOrderException("quantity must be positive");
        }
        if (!available) {
            throw new InvalidOrderException("item '" + name + "' is unavailable");
        }
        double total = price * qty;
        return roundPrice(total); // round to cents
    }

    // billable interface

    @Override
    public String getItemName() { return name; }

    @Override
    public double getUnitPrice() { return price; }

    @Override
    public String getDescription() { return description; }

    @Override
    public boolean isAvailable() { return available; }

    @Override
    public double roundPrice(double price){
        return Math.round(price * 100.0) / 100.0;
    }

    // static helpers

    // static helper that throws if item key not found
    public static MenuItem requireFound(java.util.Map<String, MenuItem> menuByKey, String key) {
        MenuItem item = (menuByKey == null) ? null : menuByKey.get(key);
        if (item == null) throw new MenuItemNotFoundException(key);
        return item;
    }

    // static helper that stops orders over the limit
    public static void enforceOrderLimit(int currentCount, int addQty, int maxItemsPerOrder) {
        if (addQty <= 0) {
            throw new InvalidOrderException("quantity must be positive");
        }
        if (currentCount + addQty > maxItemsPerOrder) {
            throw new OrderLimitExceededException(maxItemsPerOrder);
        }
    }

    // overrides

    @Override
    public String toString() {
        // shows name, price, and sold-out tag if needed
        return String.format("%s - $%.2f%s", name, price, available ? "" : " (UNAVAILABLE)");
    }

    @Override
    public boolean equals(Object o) {
        // equality check for comparing items
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem that = (MenuItem) o;
        return Double.compare(that.price, price) == 0 &&
                available == that.available &&
                name.equals(that.name) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        // so hashmaps and sets work right
        return java.util.Objects.hash(name, description, price, available);
    }
}
