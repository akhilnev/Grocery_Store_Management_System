package pharmacy.service;

public class PaymentProcessor {
    public boolean processPayment(String paymentMethod, double amount) {
        System.out.printf("\nProcessing %s payment of $%.2f\n", paymentMethod, amount);
        
        switch (paymentMethod) {
            case "INSURANCE":
                System.out.println("Verifying insurance coverage...");
                return processInsurancePayment(amount);
            case "CASH":
                return processCashPayment(amount);
            case "CARD":
                return processCardPayment(amount);
            default:
                return false;
        }
    }

    private boolean processInsurancePayment(double amount) {
        // Simplified insurance verification
        System.out.println("Insurance coverage verified");
        return true;
    }

    private boolean processCashPayment(double amount) {
        System.out.println("Cash payment received");
        return true;
    }

    private boolean processCardPayment(double amount) {
        System.out.println("Card payment processed");
        return true;
    }
}