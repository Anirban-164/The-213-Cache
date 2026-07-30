/*
An e-commerce checkout that needs to support multiple payment methods — credit card, PayPal, crypto, etc.
--> The checkout flow (validate cart, apply discount, collect payment, send receipt) is the same regardless of HOW the user pays.
--> Hardcoding if/else for each payment type inside Order means touching Order every time a new method is added.

Solution?
--> Pull the varying part (how payment happens) into its own interface family.
--> Order holds a reference to a PaymentStrategy and delegates to it — it has no idea which concrete method is used.
*/

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void pay(double amount) {
        System.out.println("Charged $" + amount + " to card ending in " + cardNumber.substring(cardNumber.length() - 4));
    }
}

class PayPalPayment implements PaymentStrategy {
    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " via PayPal account " + email);
    }
}

class CryptoPayment implements PaymentStrategy {
    private final String walletAddress;

    public CryptoPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public void pay(double amount) {
        System.out.println("Transferred $" + amount + " worth of crypto to wallet " + walletAddress);
    }
}

class Order {
    private PaymentStrategy paymentStrategy;
    private final double total;

    public Order(double total) {
        this.total = total;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout() {
        paymentStrategy.pay(total); // Order has no idea HOW payment happens
    }
}

public class PaymentSystem {
    public static void main(String[] args) {
        Order order1 = new Order(49.99);
        Order order2 = new Order(120.00);
        Order order3 = new Order(9.99);

        order1.setPaymentStrategy(new PayPalPayment("anirban@example.com"));
        order2.setPaymentStrategy(new CreditCardPayment("1234567890121234"));
        order3.setPaymentStrategy(new CryptoPayment("0xABCD...EF01"));

        order1.checkout();
        order2.checkout();
        order3.checkout();

        // Swap strategy at runtime — same order, different payment method
        order1.setPaymentStrategy(new CreditCardPayment("9999888877776666"));
        System.out.println("\n[Customer switched payment method]");
        order1.checkout();
    }
}