import java.util.ArrayList;

public class BillingService {

    public double calculateSubtotal(ArrayList<OrderItem> order) {
        double subtotal = 0;
        for (OrderItem item : order) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }

    public double calculateGST(double subtotal, double gstPercent) {
        return subtotal * gstPercent / 100;
    }

    public double calculateDiscount(double subtotal, double discountPercent) {
        return subtotal * discountPercent / 100;
    }

    public double calculateFinalTotal(double subtotal, double gstAmount, double discountAmount) {
        double total = subtotal + gstAmount - discountAmount;
        return total < 0 ? 0 : total;
    }
}