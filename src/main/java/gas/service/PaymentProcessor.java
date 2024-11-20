package payment.service;

public class PaymentProcessor {
    public boolean processCardPayment(String cardNumber, double amount) {
        // Simplified payment processing
        return cardNumber != null && cardNumber.length() >= 16;
    }
}