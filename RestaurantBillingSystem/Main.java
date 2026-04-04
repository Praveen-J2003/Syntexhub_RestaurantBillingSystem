import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static  Scanner scanner = new Scanner(System.in);
    private static  MenuService menuService = new MenuService();
    private static  BillingService billingService = new BillingService();
    private static  ReceiptService receiptService = new ReceiptService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   WELCOME TO RESTAURANT BILLING SYSTEM   ");
        System.out.println("=========================================");

        while (true) {
            displayMainMenu();
            int choice = InputValidator.getValidInt(scanner, "Enter your choice: ", 1, 3);

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    System.out.println("\nThank you for using Restaurant Billing System!");
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0); 
                    break;
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Admin - Manage Menu");
        System.out.println("2. Customer - Place Order");
        System.out.println("3. Exit");
        System.out.println("================================");
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. Add New Menu Item");
            System.out.println("2. Remove Menu Item");
            System.out.println("3. Update Item Price");
            System.out.println("4. Display All Menu Items");
            System.out.println("5. Back to Main Menu");
            System.out.println("================================");

            int choice = InputValidator.getValidInt(scanner, "Enter your choice: ", 1, 5);

            switch (choice) {
                case 1:
                    addMenuItem();
                    break;
                case 2:
                    removeMenuItem();
                    break;
                case 3:
                    updateItemPrice();
                    break;
                case 4:
                    menuService.displayAllItems();
                    break;
                case 5:
                    return;
            }
        }
    }

    private static void addMenuItem() {
        System.out.println("\n--- Add New Menu Item ---");
        
        String name = InputValidator.getValidString(scanner, "Enter item name: ");
        double price = InputValidator.getValidDouble(scanner, "Enter price (₹): ", 0, 10000);
        
        System.out.println("Select Category:");
        System.out.println("1. Starter");
        System.out.println("2. Main Course");
        System.out.println("3. Beverage");
        System.out.println("4. Dessert");
        int catChoice = InputValidator.getValidInt(scanner, "Enter category (1-4): ", 1, 4);
        
        String category = "";
        switch (catChoice) {
            case 1: category = "Starter"; break;
            case 2: category = "Main Course"; break;
            case 3: category = "Beverage"; break;
            case 4: category = "Dessert"; break;
        }
        
        MenuItem item = new MenuItem(name, price, category);
        menuService.addItem(item);
        System.out.println("✓ Item added successfully! Item ID: " + item.getItemId());
    }

    private static void removeMenuItem() {
        if (menuService.isMenuEmpty()) {
            System.out.println("Menu is empty! Nothing to remove.");
            return;
        }
        
        menuService.displayAllItems();
        int id = InputValidator.getValidInt(scanner, "Enter Item ID to remove: ", 1, Integer.MAX_VALUE);
        
        if (menuService.removeItem(id)) {
            System.out.println("✓ Item removed successfully!");
        } else {
            System.out.println("✗ Item with ID " + id + " not found!");
        }
    }

    private static void updateItemPrice() {
        if (menuService.isMenuEmpty()) {
            System.out.println("Menu is empty! Nothing to update.");
            return;
        }
        
        menuService.displayAllItems();
        int id = InputValidator.getValidInt(scanner, "Enter Item ID to update price: ", 1, Integer.MAX_VALUE);
        double newPrice = InputValidator.getValidDouble(scanner, "Enter new price (₹): ", 0, 10000);
        
        if (menuService.updatePrice(id, newPrice)) {
            System.out.println("✓ Price updated successfully!");
        } else {
            System.out.println("✗ Item with ID " + id + " not found!");
        }
    }

    private static void customerMenu() {
        if (menuService.isMenuEmpty()) {
            System.out.println("\nSorry! Menu is empty. Please contact admin.");
            return;
        }

        ArrayList<OrderItem> currentOrder = new ArrayList<>();
        
        System.out.println("\n========== CUSTOMER ORDER ==========");
        
        while (true) {
            menuService.displayAllItems();
            System.out.println("\n0. Finish Order");
            
            int itemId = InputValidator.getValidInt(scanner, "Enter Item ID to add to order: ", 0, Integer.MAX_VALUE);
            
            if (itemId == 0) {
                break;
            }
            
            MenuItem selectedItem = menuService.getItemById(itemId);
            if (selectedItem == null) {
                System.out.println("✗ Invalid Item ID! Please try again.");
                continue;
            }
            
            int quantity = InputValidator.getValidInt(scanner, "Enter quantity: ", 1, 100);
            
            OrderItem orderItem = new OrderItem(selectedItem, quantity);
            currentOrder.add(orderItem);
            System.out.println("✓ Added: " + selectedItem.getName() + " x" + quantity);
        }
        
        if (currentOrder.isEmpty()) {
            System.out.println("No items ordered. Returning to main menu.");
            return;
        }
        
        // Calculate bill
        double subtotal = billingService.calculateSubtotal(currentOrder);
        
        System.out.print("\nApply discount? (Y/N): ");
        boolean applyDiscount = InputValidator.getValidYesNo(scanner);
        double discountPercent = 0;
        
        if (applyDiscount) {
            discountPercent = InputValidator.getValidDouble(scanner, "Enter discount percentage: ", 0, 100);
        }
        
        double gstPercent = 5.0; // 5% GST
        double gstAmount = billingService.calculateGST(subtotal, gstPercent);
        double discountAmount = billingService.calculateDiscount(subtotal, discountPercent);
        double finalTotal = billingService.calculateFinalTotal(subtotal, gstAmount, discountAmount);
        
        // Generate and display receipt
        String receipt = receiptService.generateReceiptString(currentOrder, subtotal, gstAmount, discountAmount, finalTotal);
        receiptService.printReceiptToConsole(receipt);
        
        // Save receipt to file
        receiptService.saveReceiptToFile(receipt);
    }
}