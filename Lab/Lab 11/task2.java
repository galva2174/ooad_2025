import java.util.Scanner;

interface Payment {
    void processPayment(double amount);

    double getDiscount(double amount);

    String getConfirmation(String srn);
}

interface PaymentFactory {
    Payment createPayment(String type);
}

class VisaPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Visa payment of $" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return amount * 0.02;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - Visa payment processed successfully!with 2% discount";
    }
}

class MasterCardPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing MasterCard payment of $" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return amount * 0.015;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - MasterCard payment processed successfully! with 1.5% discount";
    }
}

class CreditCardFactory implements PaymentFactory {
    @Override
    public Payment createPayment(String type) {
        if (type.equalsIgnoreCase("VISA")) {
            return new VisaPayment();
        } else if (type.equalsIgnoreCase("MASTERCARD")) {
            return new MasterCardPayment();
        }
        throw new IllegalArgumentException("Unknown credit card type: " + type);
    }
}

class GooglePayPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Google Pay payment of ₹" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return amount * 0.05;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - Google Pay payment processed successfully! with 5% discount";
    }
}

class PhonePePayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PhonePe payment of ₹" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return amount * 0.05;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - PhonePe payment processed successfully! with 5% discount";
    }
}

// Concrete Factory for UPI
class UPIFactory implements PaymentFactory {
    @Override
    public Payment createPayment(String type) {
        if (type.equalsIgnoreCase("GOOGLEPAY")) {
            return new GooglePayPayment();
        } else if (type.equalsIgnoreCase("PHONEPE")) {
            return new PhonePePayment();
        }
        throw new IllegalArgumentException("Unknown UPI type: " + type);
    }
}

class CashPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Cash on Delivery payment of ₹" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return 0;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - Cash on Delivery order confirmed!";
    }
}

class WalletPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Wallet payment of ₹" + amount);
    }

    @Override
    public double getDiscount(double amount) {
        return amount * 0.03;
    }

    @Override
    public String getConfirmation(String srn) {
        return srn + " - Wallet payment processed successfully!";
    }
}

class PaymentOnDeliveryFactory implements PaymentFactory {
    @Override
    public Payment createPayment(String type) {
        if (type.equalsIgnoreCase("CASH")) {
            return new CashPayment();
        } else if (type.equalsIgnoreCase("WALLET")) {
            return new WalletPayment();
        }
        throw new IllegalArgumentException("Unknown delivery payment type: " + type);
    }
}

class PaymentGateway {
    private final String SRN = "PES1UG22CS360";

    public void processTransaction() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available Payment Methods:");
        System.out.println("1. Credit Card");
        System.out.println("2. UPI");
        System.out.println("3. Payment on Delivery");
        System.out.print("Select payment method (1-3): ");

        int method = scanner.nextInt();
        scanner.nextLine();

        PaymentFactory factory = null;
        String paymentType = "";

        switch (method) {
            case 1:
                factory = new CreditCardFactory();
                System.out.println("Available Credit Cards:");
                System.out.println("- Visa");
                System.out.println("- MasterCard");
                System.out.print("Select card type: ");
                paymentType = scanner.nextLine();
                break;

            case 2:
                factory = new UPIFactory();
                System.out.println("Available UPI Options:");
                System.out.println("- GooglePay");
                System.out.println("- PhonePe");
                System.out.print("Select UPI type: ");
                paymentType = scanner.nextLine();
                break;

            case 3:
                factory = new PaymentOnDeliveryFactory();
                System.out.println("Available Delivery Options:");
                System.out.println("- Cash");
                System.out.println("- Wallet");
                System.out.print("Select delivery payment type: ");
                paymentType = scanner.nextLine();
                break;

            default:
                System.out.println("Invalid payment method selected");
                scanner.close();
                return;
        }

        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();

        try {
            Payment payment = factory.createPayment(paymentType);
            payment.processPayment(amount);

            double discount = payment.getDiscount(amount);
            double finalAmount = amount - discount;

            System.out.println("Discount applied: " + discount);
            System.out.println("Final amount: " + finalAmount);
            System.out.println(payment.getConfirmation(SRN));

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}

public class task2 {
    public static void main(String[] args) {
        PaymentGateway gateway = new PaymentGateway();
        gateway.processTransaction();
    }
}