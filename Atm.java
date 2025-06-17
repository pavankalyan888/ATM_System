
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Account {

    private String username;
    private String pin;
    private String location;
    private String accountNumber;
    private Double balance;

    Account(String pin, String username, String location, String accountNumber, Double balance) {
        this.pin = pin;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.location = location;
        this.username = username;
    }

    public String getpin() {
        return pin;
    }

    public void setpin(String pin) {
        this.pin = pin;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getaccountNumber() {
        return accountNumber;
    }

    public void setaccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public Double getbalance() {
        return balance;
    }

    public void setbalance(Double balance) {
        this.balance = balance;
    }

    public void userInfo() {
        System.out.println(getusername());
        System.out.println(getaccountNumber());
        System.out.println(getlocation());
        System.out.println(getbalance());
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void Deposit(double amount) {
        this.balance += amount;
    }
}

class Admin {

    private static final String ADMIN_PIN = "1234";

    public boolean login(String pin) {
        return ADMIN_PIN.equals(pin);
    }

    public void viewAllAccounts(Map<String, Account> accounts) {
        // Logic to display all user accounts
        for (Account acc : accounts.values()) {
            System.out.println("user " + acc.getusername() + ", AccountNumber " + acc.getaccountNumber());
        }
    }

    public void deleteAccount(Map<String, Account> accounts, String accNum) {
        // Logic to remove an account
        if (accounts.containsKey(accNum)) {
            accounts.remove(accNum);
            System.out.println("Account deleted");
        } else {
            System.out.println("Account not found");
        }
    }

    public void adminLogout() {
        System.out.println("Admin logged out");
    }

}

class TransactionValidator {

    public boolean isValidAmount(double amount) {
        return amount > 0 && amount % 100 == 0;
    }

    public boolean hasSufficientBalance(double balance, double amount) {
        return balance >= amount;
    }
}

class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidAmountException extends Exception {

    public InvalidAmountException(String message) {
        super(message);
    }
}

public class Atm {

    private static Map<String, Account> accounts = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);
    private static Admin admin = new Admin();
    private static TransactionValidator validator = new TransactionValidator();
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void main(String[] args) {
        ArrayList<String> keyList = new ArrayList<>(accounts.keySet());
        System.out.println("Account Number: " + keyList);
        while (true) {
            System.out.println(BLUE + "\n----- Welcome to ATM System -----" + RESET);
            System.out.println("1. Create Account");
            System.out.println("2. Login as Account Holder");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.print(BLUE + "Enter your choice: " + RESET);
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    userLogin();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    exitSystem();
                    return;
                default:
                    System.out.println(RED + "Invalid choice." + RESET);
            }
        }
    }

    public static Account authenticateUser(String accountNumber, String pin) {
        Account acc = accounts.get(accountNumber);
        return (acc != null && acc.getpin().equals(pin)) ? acc : null;
    }

    private static void createAccount() {
        // accounts.clear();
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter your account number: ");
        String accountNumber = sc.nextLine();
        System.out.print("Enter your pin: ");
        String pin = sc.nextLine();
        System.out.println("Enter the Location: ");
        String location = sc.nextLine();
        System.out.print("Enter your balance: ");
        Double balance = sc.nextDouble();

        Account acc = new Account(pin, name, location, accountNumber, balance);
        accounts.put(accountNumber, acc);
        System.out.println(GREEN + "Account created successfully" + RESET);
        System.out.println("Account Number: " + accountNumber);
    }

    private static void userLogin() {
        sc.nextLine();
        System.out.println("Enter Your Account number: ");
        String accnum = sc.nextLine();
        System.out.println("Enter Your Pin: ");
        String pin = sc.nextLine();

        Account acc = authenticateUser(accnum, pin);
        if (acc != null) {
            System.out.println(GREEN + "Welcome " + acc.getusername() + RESET);
            userAccountMenu(acc);
        } else {
            System.out.println(RED + "Invalid Account Number or Pin" + RESET);
        }
    }

    public static void adminLogin() {
        sc.nextLine();
        System.out.println("Enter your admin pin: ");
        String pin = sc.nextLine();
        if (admin.login(pin)) {
            System.out.println(YELLOW + "Welcome Admin" + RESET);
            //admin menu
        } else {
            System.out.println(RED + "Invalid Admin Pin!" + RESET);
        }

    }

    public static void exitSystem() {
        System.out.println(YELLOW + "Exiting Atm System successful..!" + RESET);
        System.exit(0);

    }

    public static void adminMenu(Map<String, Account> accounts) {
        System.out.println(BLUE + "---***---Welcome to Admin Menu---***---" + RESET);
        System.out.println("1. View All Accounts");
        System.out.println("2. Delete Account");
        System.out.println("3. Logout");
        System.out.println("Enter your choice: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                admin.viewAllAccounts(accounts);
                break;
            case 2:
                System.out.println(YELLOW + "Enter Account Number to delete: " + RESET);
                String accnum = sc.nextLine();
                sc.nextLine();
                admin.deleteAccount(accounts, accnum);
                break;
            case 3:
                admin.adminLogout();
                break;
            default:
                System.out.println(RED + "Incorrect admin operations!" + RESET);
        }
    }

    public static void userAccountMenu(Account acc) {
        System.out.println(BLUE + "---***---Welcome to Your Account Menu---***---" + RESET);
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Logout");
        System.out.println("Enter your choice: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                checkBalance(acc);
                break;
            case 2:
                System.out.println("Enter the ammount");
                Double amount = sc.nextDouble();
                try {
                    Deposite(acc, amount);
                } catch (InvalidAmountException e) {
                    System.out.println(RED + "Error: " + e.getMessage() + RESET);
                }
                break;
            case 3:
                System.err.println("Enter the amount");
                Double amount1 = sc.nextDouble();
                try {
                    withdraw(acc, amount1);
                } catch (InvalidAmountException | InsufficientFundsException e) {
                    System.out.println(RED + "Error: " + e.getMessage() + RESET);
                }
                break;

            case 4:
                System.out.println(GREEN + "Successfully logedout from the account!" + RESET);
            default:
                System.err.println(RED + "Incorrect user operations!!" + RESET);
        }
    }

    public static void Deposite(Account acc, Double amount) throws InvalidAmountException {
        if (!validator.isValidAmount(amount)) {
            // System.out.println(RED + "Invalid Amount" + RESET);
            throw new InvalidAmountException("Withdrawal amount must be greater than 0.");
        } else {
            acc.Deposit(amount);
            System.out.println(GREEN + "Deposited successfully and current balance is: " + acc.getbalance() + RESET);
        }
    }

    public static void withdraw(Account acc, Double amount) throws InvalidAmountException, InsufficientFundsException {
        if (!validator.isValidAmount(amount)) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0.");
        } else if (!validator.hasSufficientBalance(acc.getbalance(), amount)) {
            throw new InsufficientFundsException("Insufficient balance. Current balance: " + acc.getbalance());
        } else {
            acc.withdraw(amount);
            System.out.println("Successfully done withdrawel and current balance is: " + acc.getbalance());
        }
    }

    public static void checkBalance(Account acc) {
        System.out.println("Your current balance is: " + acc.getbalance());
    }
}
