package coffeeshop;

public class Customer extends Person {
    private static int instanceCount = 0;
    public static final int MAX_CUSTOMERS = 100;
    
    private int loyaltyPoints;

    public Customer(String name, String phone) {
        super(name, phone);
        if (instanceCount >= MAX_CUSTOMERS) {
            throw new IllegalStateException("Cannot create more than " + MAX_CUSTOMERS + " customers");
        }
        this.loyaltyPoints = 0;
        instanceCount++;
    }
    @Override
    public void greet() {
        String n = getName();
        if (n == null) n = "";
        String message = "Hello " + n + ", welcome to Java Beans Cafe";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) sb.append(message.charAt(i));
        System.out.println(sb.toString());
    }
    public void addPoints(int points) {
        int p = points;
        if (p < 0) p = 0;
        int total = this.loyaltyPoints + p;
        if (total < 0) total = 0;
        this.loyaltyPoints = total;
    }
    public void redeemPoints(int used) {
        int u = used;
        if (u < 0) u = 0;
        int current = this.loyaltyPoints;
        if (u > current) u = current;
        this.loyaltyPoints = current - u;
    }
    public int getPoints() {
        return this.loyaltyPoints;;
    }
    public boolean hasEnoughPoints(int threshold) {
        int t = threshold;
        if (t < 0) t = 0;
        int lp = getPoints();
        return lp >= t;
    }

    public void deletePoints(){
        this.loyaltyPoints = 0;
    }

    public void mergeLoyalty(Customer other) {
        if (other == null) return;
        int add = other.getPoints();
        if (add < 0){
            throw new IllegalArgumentException("Points can’t be negative");
        }
        this.loyaltyPoints += add;
        //added logic to delete the points from other account/customer when merging
        other.deletePoints();
    }
    @Override
    public String toString() {
        String base = super.toString();
        int pts = getPoints();
        String s = String.valueOf(pts);
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append(" | Loyalty Points: ");
        for (int i = 0; i < s.length(); i++) sb.append(s.charAt(i));
        return sb.toString();
    }
    public boolean equalsCustomer(Customer other) {
        if (other == null) return false;
        if (!equalsByName(other)) return false;
        if (!equalsByPhone(other)) return false;
        return true;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        String cleanedPhone = phone.replaceAll("[\\s\\-\\(\\)\\.]", "");
        return cleanedPhone.matches("\\d{10,}");
    }
    public static int getInstanceCount() { 
        return instanceCount; 
    }
    
    public static int getRemainingCapacity() { 
        return MAX_CUSTOMERS - instanceCount; 
    }
}