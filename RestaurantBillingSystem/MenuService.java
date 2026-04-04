import java.util.ArrayList;

public class MenuService {
    private ArrayList<MenuItem> menuItems;

    public MenuService() {
        menuItems = new ArrayList<>();
        loadSampleMenu();
    }

    private void loadSampleMenu() {
        menuItems.add(new MenuItem("Burger", 99.00, "Main Course"));
        menuItems.add(new MenuItem("Pizza", 199.00, "Main Course"));
        menuItems.add(new MenuItem("French Fries", 49.00, "Starter"));
        menuItems.add(new MenuItem("Coke", 30.00, "Beverage"));
        menuItems.add(new MenuItem("Ice Cream", 59.00, "Dessert"));
        menuItems.add(new MenuItem("Pasta", 149.00, "Main Course"));
        menuItems.add(new MenuItem("Garlic Bread", 39.00, "Starter"));
        menuItems.add(new MenuItem("Coffee", 45.00, "Beverage"));
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    public boolean removeItem(int itemId) {
        return menuItems.removeIf(item -> item.getItemId() == itemId);
    }

    public boolean updatePrice(int itemId, double newPrice) {
        for (MenuItem item : menuItems) {
            if (item.getItemId() == itemId) {
                item.setPrice(newPrice);
                return true;
            }
        }
        return false;
    }

    public void displayAllItems() {
        if (menuItems.isEmpty()) {
            System.out.println("\n--- Menu is empty! ---");
            return;
        }
        
        System.out.println("\n========== RESTAURANT MENU ==========");
        System.out.println("ID    Name                  Price      Category");
        System.out.println("-----------------------------------------------");
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
        System.out.println("===============================================");
    }

    public MenuItem getItemById(int id) {
        for (MenuItem item : menuItems) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    public boolean isMenuEmpty() {
        return menuItems.isEmpty();
    }

    public ArrayList<MenuItem> getAllItems() {
        return menuItems;
    }
}