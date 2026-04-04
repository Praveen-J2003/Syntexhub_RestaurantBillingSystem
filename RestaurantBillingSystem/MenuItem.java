public class MenuItem {
    private static int nextId = 1;
    private int itemId;
    private String name;
    private double price;
    private String category;

    public MenuItem(String name, double price, String category) {
        this.itemId = nextId++;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters
    public int getItemId() { return itemId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("%-5d %-20s ₹%-8.2f %-12s", itemId, name, price, category);
    }
}