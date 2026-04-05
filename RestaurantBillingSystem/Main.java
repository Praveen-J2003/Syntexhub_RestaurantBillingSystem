import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MenuService menuService = new MenuService();
    private static BillingService billingService = new BillingService();
    private static ReceiptService receiptService = new ReceiptService();
    private static ArrayList<OrderItem> currentOrder = new ArrayList<>();

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
                    System.out.println("\n✨ Thank you for using Restaurant Billing System!");
                    System.out.println("👋 Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("              MAIN MENU                   ");
        System.out.println("==========================================");
        System.out.println("  1. 👨‍💼 Admin - Manage Menu");
        System.out.println("  2. 🧑‍🍳 Customer - Place Order");
        System.out.println("  3. 🚪 Exit");
        System.out.println("==========================================");
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("              ADMIN MENU                  ");
            System.out.println("==========================================");
            System.out.println("  1. ➕ Add New Menu Item");
            System.out.println("  2. ❌ Remove Menu Item");
            System.out.println("  3. 📝 Update Item Price");
            System.out.println("  4. 📋 Display All Menu Items");
            System.out.println("  5. 🔙 Back to Main Menu");
            System.out.println("==========================================");

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
        System.out.println("\n------------------------------------------");
        System.out.println("           ADD NEW MENU ITEM              ");
        System.out.println("------------------------------------------");
        
        String name = InputValidator.getValidString(scanner, "📝 Enter item name: ");
        double price = InputValidator.getValidDouble(scanner, "💰 Enter price (₹): ", 0, 10000);
        
        System.out.println("\n📂 Select Category:");
        System.out.println("   1. 🍢 Starter");
        System.out.println("   2. 🍛 Main Course");
        System.out.println("   3. 🥤 Beverage");
        System.out.println("   4. 🍰 Dessert");
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
        System.out.println("\n✅ Item added successfully! Item ID: " + item.getItemId());
    }

    private static void removeMenuItem() {
        if (menuService.isMenuEmpty()) {
            System.out.println("\n⚠️ Menu is empty! Nothing to remove.");
            return;
        }
        
        menuService.displayAllItems();
        int id = InputValidator.getValidInt(scanner, "\n🗑️ Enter Item ID to remove: ", 1, Integer.MAX_VALUE);
        
        if (menuService.removeItem(id)) {
            System.out.println("\n✅ Item removed successfully!");
        } else {
            System.out.println("\n❌ Item with ID " + id + " not found!");
        }
    }

    private static void updateItemPrice() {
        if (menuService.isMenuEmpty()) {
            System.out.println("\n⚠️ Menu is empty! Nothing to update.");
            return;
        }
        
        menuService.displayAllItems();
        int id = InputValidator.getValidInt(scanner, "\n📝 Enter Item ID to update price: ", 1, Integer.MAX_VALUE);
        double newPrice = InputValidator.getValidDouble(scanner, "💰 Enter new price (₹): ", 0, 10000);
        
        if (menuService.updatePrice(id, newPrice)) {
            System.out.println("\n✅ Price updated successfully!");
        } else {
            System.out.println("\n❌ Item with ID " + id + " not found!");
        }
    }

    private static void customerMenu() {
        if (menuService.isMenuEmpty()) {
            System.out.println("\n⚠️ Sorry! Menu is empty. Please contact admin.");
            return;
        }

        currentOrder.clear();
        
        System.out.println("\n==========================================");
        System.out.println("        🧑‍🍳 CUSTOMER ORDER MENU          ");
        System.out.println("==========================================");
        
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("              OPTIONS                    ");
            System.out.println("==========================================");
            System.out.println("  1. 📋 Browse & Add Items");
            System.out.println("  2. 🗑️ Remove Item from Order");
            System.out.println("  3. 📊 View Order Summary");
            System.out.println("  4. 🔄 Clear Entire Order");
            System.out.println("  5. 💳 Generate Bill & Pay");
            System.out.println("  6. 🔙 Back to Main Menu");
            System.out.println("==========================================");
            
            int choice = InputValidator.getValidInt(scanner, "\n👉 Enter your choice: ", 1, 6);
            
            switch (choice) {
                case 1:
                    addItemsToOrder();
                    break;
                case 2:
                    removeItemFromOrder();
                    break;
                case 3:
                    displayOrderSummary();
                    break;
                case 4:
                    clearOrder();
                    break;
                case 5:
                    if (currentOrder.isEmpty()) {
                        System.out.println("\n⚠️ Your order is empty! Please add items first.");
                    } else {
                        generateBill();
                        return;
                    }
                    break;
                case 6:
                    if (!currentOrder.isEmpty()) {
                        System.out.print("\n⚠️ You have items in your order. Clear order? (Y/N): ");
                        if (InputValidator.getValidYesNo(scanner)) {
                            currentOrder.clear();
                            System.out.println("✅ Order cleared. Returning to main menu.");
                        } else {
                            continue;
                        }
                    }
                    return;
            }
        }
    }

    private static void displayOrderSummary() {
        if (currentOrder.isEmpty()) {
            System.out.println("\n📭 Your order is empty");
            return;
        }
        
        System.out.println("\n==========================================");
        System.out.println("           📋 ORDER SUMMARY               ");
        System.out.println("==========================================");
        System.out.printf("%-20s %5s %10s\n", "Item", "Qty", "Total(₹)");
        System.out.println("------------------------------------------");
        
        double runningTotal = 0;
        for (int i = 0; i < currentOrder.size(); i++) {
            OrderItem item = currentOrder.get(i);
            double itemTotal = item.getTotalPrice();
            runningTotal += itemTotal;
            System.out.printf("%-20s %5d %10.2f\n", 
                item.getMenuItem().getName(), 
                item.getQuantity(), 
                itemTotal);
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-26s %10.2f\n", "Subtotal:", runningTotal);
        System.out.println("==========================================");
    }

    private static void addItemsToOrder() {
        while (true) {
            menuService.displayAllItems();
            
            int itemId = InputValidator.getValidInt(scanner, "\n🔢 Enter Item ID: ", 1, Integer.MAX_VALUE);
            
            MenuItem selectedItem = menuService.getItemById(itemId);
            if (selectedItem == null) {
                System.out.println("\n❌ Invalid Item ID! Please try again.");
                continue;
            }
            
            int quantity = InputValidator.getValidInt(scanner, "🔢 Enter quantity (1-100): ", 1, 100);
            
            boolean found = false;
            for (OrderItem item : currentOrder) {
                if (item.getMenuItem().getItemId() == itemId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    System.out.println("\n✅ Updated: " + selectedItem.getName() + " x" + item.getQuantity());
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                OrderItem orderItem = new OrderItem(selectedItem, quantity);
                currentOrder.add(orderItem);
                System.out.println("\n✅ Added: " + selectedItem.getName() + " x" + quantity);
            }
            
            System.out.print("\n➕ Add more items? (Y/N): ");
            if (!InputValidator.getValidYesNo(scanner)) {
                break;
            }
        }
        
        if (!currentOrder.isEmpty()) {
            System.out.print("\n📊 View order summary? (Y/N): ");
            if (InputValidator.getValidYesNo(scanner)) {
                displayOrderSummary();
            }
        }
    }

    private static void removeItemFromOrder() {
        if (currentOrder.isEmpty()) {
            System.out.println("\n⚠️ Your order is empty! Nothing to remove.");
            return;
        }
        
        displayOrderSummary();
        System.out.print("\n🔢 Enter item number to remove (1-" + currentOrder.size() + "): ");
        int index = InputValidator.getValidInt(scanner, "", 1, currentOrder.size());
        
        OrderItem removed = currentOrder.remove(index - 1);
        System.out.println("\n✅ Removed: " + removed.getMenuItem().getName());
        
        System.out.print("\n📊 View updated summary? (Y/N): ");
        if (InputValidator.getValidYesNo(scanner)) {
            displayOrderSummary();
        }
    }

    private static void clearOrder() {
        if (currentOrder.isEmpty()) {
            System.out.println("\n⚠️ Your order is already empty!");
            return;
        }
        
        System.out.print("\n⚠️ Are you sure you want to clear entire order? (Y/N): ");
        if (InputValidator.getValidYesNo(scanner)) {
            currentOrder.clear();
            System.out.println("\n✅ Order cleared successfully!");
        }
    }

    private static void generateBill() {
        System.out.println("\n==========================================");
        System.out.println("           💳 BILLING SECTION             ");
        System.out.println("==========================================");
        
        displayOrderSummary();
        
        double subtotal = billingService.calculateSubtotal(currentOrder);
        
        System.out.println("\n==========================================");
        System.out.println("           🏷️ DISCOUNT OPTIONS            ");
        System.out.println("==========================================");
        System.out.println("  1. No Discount");
        System.out.println("  2. Apply Discount");
        System.out.println("==========================================");
        
        int discountChoice = InputValidator.getValidInt(scanner, "\n👉 Select discount option (1-2): ", 1, 2);
        double discountPercent = 0;
        
        if (discountChoice == 2) {
            discountPercent = InputValidator.getValidDouble(scanner, "🏷️ Enter discount percentage (0-100): ", 0, 100);
        }
        
        System.out.println("\n🔄 Calculating bill...");
        
        double gstPercent = 5.0;
        double gstAmount = billingService.calculateGST(subtotal, gstPercent);
        double discountAmount = billingService.calculateDiscount(subtotal, discountPercent);
        double finalTotal = billingService.calculateFinalTotal(subtotal, gstAmount, discountAmount);
        
        String receipt = receiptService.generateReceiptString(currentOrder, subtotal, gstAmount, discountAmount, finalTotal);
        receiptService.printReceiptToConsole(receipt);
        receiptService.saveReceiptToFile(receipt);
        
        System.out.println("\n✅ Bill generated successfully!");
        System.out.print("\n🔄 Start new order? (Y/N): ");
        if (InputValidator.getValidYesNo(scanner)) {
            currentOrder.clear();
            customerMenu();
        }
    }
}
