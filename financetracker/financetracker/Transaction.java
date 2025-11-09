public class Transaction {
    private String description;
    private double amount;
    private String category;
    private String type; // "Income" or "Expense"
    private String date;

    // Constructor to initialize the transaction details
    public Transaction(String description, double amount, String category, String type, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    // Getter methods for each attribute
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
