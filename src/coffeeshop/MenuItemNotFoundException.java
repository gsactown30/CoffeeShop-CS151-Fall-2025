package coffeeshop;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String key) {
        super("Menu item not found: " + key);
    }
}
