package coffeeshop;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int counter = 1;
    private int orderId;
    private Customer customer;
    private List<Billable> items;
    private List<Integer> quantities;
    private String status;
    public static final int MAX_ITEMS_PER_ORDER = 10;
    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.status = "Pending";
        this.orderId = nextId();
    }
    private static synchronized int nextId() {
        int c = counter;
        counter = c + 1;
        return c;
    }
    public int getOrderId() {
        return orderId;
    }
    public String getStatus() {
        if (status == null) return "";
        return status;
    }
    public void setStatus(String s) {
        String v = s == null ? "" : s.trim();
        if (v.isEmpty()) v = "Pending";
        this.status = v;
    }
    public Customer getCustomer() {
        return customer;
    }
    public boolean addItem(Billable menuItem, int qty) {
        if (menuItem == null) return false;
        if (qty <= 0) return false;
        if (this.items.size() >= MAX_ITEMS_PER_ORDER) return false;
        this.items.add(menuItem);
        this.quantities.add(qty);
        return true;
    }
    public double getTotal() {
        double sum = 0.0;
        for (int i = 0; i < items.size(); i++) {
            Billable item = items.get(i);
            int q = quantities.get(i);
            sum += item.calculateTotal(q);
        }
        return roundPrice(sum);
    }
    public int getItemCount() {
        return items.size();
    }

    //helper method to round price
    public double roundPrice(double price){
        return Math.round(price * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        String cid = String.valueOf(getOrderId());
        String st = getStatus();
        String cust = customer == null ? "" : customer.toString();
        String total = String.format("%.2f", getTotal());
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(cid);
        sb.append(" [").append(st).append("] ");
        sb.append(" | Total: $").append(total);
        sb.append(" | ").append(cust);
        return sb.toString();
    }
}

