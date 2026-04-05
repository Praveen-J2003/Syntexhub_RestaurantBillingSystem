import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReceiptService {

    public String generateReceiptString(ArrayList<OrderItem> order, double subtotal, 
                                         double gstAmount, double discountAmount, double finalTotal) {
        
        StringBuilder receipt = new StringBuilder();
        
        receipt.append("\n");
        receipt.append("=============================================\n");
        receipt.append("              RESTAURANT BILL                \n");
        receipt.append("=============================================\n");
        receipt.append(String.format("%-20s %5s %10s %12s\n", "Item", "Qty", "Price (₹)", "Total (₹)"));
        receipt.append("---------------------------------------------\n");
        
        for (OrderItem item : order) {
            receipt.append(String.format("%-20s %5d %10.2f %12.2f\n",
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getMenuItem().getPrice(),
                    item.getTotalPrice()));
        }
        
        receipt.append("---------------------------------------------\n");
        receipt.append(String.format("%-30s %12.2f\n", "Subtotal (₹):", subtotal));
        receipt.append(String.format("%-30s %12.2f\n", "GST (5%) (₹):", gstAmount));
        receipt.append(String.format("%-30s %12.2f\n", "Discount (₹):", discountAmount));
        receipt.append("=============================================\n");
        receipt.append(String.format("%-30s %12.2f\n", "FINAL TOTAL (₹):", finalTotal));
        receipt.append("=============================================\n");
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        receipt.append("Date & Time: ").append(dtf.format(LocalDateTime.now())).append("\n");
        receipt.append("=============================================\n");
        receipt.append("         Thank you! Visit Again!             \n");
        receipt.append("=============================================\n");
        
        return receipt.toString();
    }

    public void printReceiptToConsole(String receipt) {
        System.out.println(receipt);
    }

    public void saveReceiptToFile(String receiptContent) {
        try {
            java.io.File receiptsDir = new java.io.File("receipts");
            if (!receiptsDir.exists()) {
                receiptsDir.mkdir();
            }
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String fileName = "receipts/receipt_" + dtf.format(LocalDateTime.now()) + ".txt";
            
            FileWriter writer = new FileWriter(fileName);
            writer.write(receiptContent);
            writer.close();
            
            System.out.println("✓ Receipt saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("✗ Error saving receipt: " + e.getMessage());
        }
    }
}
