import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Bank {
    private String bankName;
    private List<Account> accounts;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
    }
    
    public Account findAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }
    
    public void closeAccount(int accountNumber) {
    Account account = findAccount(accountNumber);
    if (account != null) {
        System.out.println("Are you sure you want to close this account? (Type 'yes' to confirm, 'no' to cancel): ");
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            // Return the deposited amount to the user
            double balance = account.getBalance();
            System.out.println("Account closed. You have received a final balance of P" + balance);

            // Remove the account from the list
            accounts.remove(account);

            // Delete the account file
            File accountFile = new File(accountNumber + ".txt");
            if (accountFile.delete()) {
                System.out.println("Account file deleted.");
            } else {
                System.out.println("Error deleting account file.");
            }
        } else {
            System.out.println("Account closure canceled.");
        }
    } else {
        System.out.println("Account not found.");
    }
}


    public void openAccount(Scanner scanner) {
    System.out.println("Enter First Name: ");
    String firstName = scanner.nextLine();
    System.out.println("Enter Middle Name: ");
    String middleName = scanner.nextLine();
    System.out.println("Enter Last Name: ");
    String lastName = scanner.nextLine();
    System.out.println("Enter Address: ");
    String address = scanner.nextLine();
    System.out.println("Enter Birthday (MM/DD/YYYY): ");
    String birthday = scanner.nextLine();
    System.out.println("Enter Gender (M/F): ");
    char gender = scanner.nextLine().charAt(0);
    System.out.println("Enter Account Type (Savings Account or Current Account): ");
    String accountType = scanner.nextLine();
    System.out.println("Enter Initial Deposit: ");
    double initialDeposit = scanner.nextDouble();
    System.out.println("Enter PIN (6-digit number): ");
    int pin = scanner.nextInt();

    // Validate user input
    if (!isValidAge(birthday)) {
        System.out.println("Sorry, you must be at least 18 years old to open an account.");
        return;
    }

    if (gender != 'M' && gender != 'F') {
        System.out.println("Invalid gender input. Please enter 'M' or 'F'.");
        return;
    }

    if (!isValidInitialDeposit(accountType, initialDeposit)) {
        System.out.println("Initial deposit does not meet the requirements.");
        return;
    }

    int accountNumber = generateAccountNumber();
    Account account = new Account(accountNumber, firstName, lastName, middleName, address, birthday, gender, accountType, initialDeposit, pin);
    accounts.add(account);

    // Display the account number
    System.out.println("Account created successfully. Your Account Number is: " + accountNumber);

    // Create a file for the account
    try {
        FileWriter fileWriter = new FileWriter(accountNumber + ".txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(account.toString());
        printWriter.close();
    } catch (IOException e) {
        System.out.println("Error creating account file.");
    }
}

    private int generateAccountNumber() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private boolean isValidAge(String birthday) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date birthDate = dateFormat.parse(birthday);
            Calendar cal = Calendar.getInstance();
            cal.setTime(birthDate);
            int birthYear = cal.get(Calendar.YEAR);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            
            int age = currentYear - birthYear;
            
            return age >= 18;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidInitialDeposit(String accountType, double initialDeposit) {
        if (accountType.equalsIgnoreCase("Savings Account")) {
            return initialDeposit >= 100.0;
        } else if (accountType.equalsIgnoreCase("Current Account")) {
            return initialDeposit >= 500.0;
        }
        return false;
    }

}

class Account {
    private int accountNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String birthday;
    private char gender;
    private String accountType;
    private double initialDeposit;
    private double balance;
    private int pin;

    public Account(int accountNumber, String firstName, String lastName, String middleName, String address, String birthday, char gender, String accountType, double initialDeposit, int pin) {
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
        this.balance = initialDeposit;
        this.pin = pin;
    }


    public int getAccountNumber() {
        return accountNumber;
    }

    public double getMinDepositAmount() {
        if ("Savings Account".equals(accountType)) {
            return 100.0; // Example: Minimum deposit for a Savings Account
        } else if ("Current Account".equals(accountType)) {
            return 500.0; // Example: Minimum deposit for a Current Account
        }
        return 0.0; // Default minimum deposit amount
    }
    
    public double getMinWithdrawAmount() {
        if ("Savings Account".equals(accountType)) {
            return 10.0; // Example: Minimum withdrawal for a Savings Account
        } else if ("Current Account".equals(accountType)) {
            return 50.0; // Example: Minimum withdrawal for a Current Account
        }
        return 0.0; // Default minimum withdrawal amount
    }


    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit of P" + amount + " successful. New balance: P" + balance);
        } else {
            System.out.println("Invalid deposit amount. Please enter a positive amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal of P" + amount + " successful. New balance: P" + balance);
        } else if (amount > balance) {
            System.out.println("Insufficient funds. Your balance is P" + balance);
        } else {
            System.out.println("Invalid withdrawal amount. Please enter a valid amount.");
        }
    }


    
     public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public char getGender() {
        return gender;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getInitialDeposit() {
        return initialDeposit;
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }


    public String toString() {
        // Generate a string representation of the account
        return "Account Number: " + accountNumber + "\n" +
                "Full Name: " + firstName + " " + middleName + " " + lastName + "\n" +
                "Address: " + address + "\n" +
                "Birthday: " + birthday + "\n" +
                "Gender: " + gender + "\n" +
                "Account Type: " + accountType + "\n" +
                "Initial Deposit: " + initialDeposit + "\n" +
                "Current Balance: " + balance;
    }
}

class SavingsAccount extends Account {
    public SavingsAccount(int accountNumber, String firstName, String lastName, String middleName, String address, String birthday, char gender, double initialDeposit, int pin) {
        super(accountNumber, firstName, lastName, middleName, address, birthday, gender, "Savings Account", initialDeposit, pin);
    }
}

class CurrentAccount extends Account {
    public CurrentAccount(int accountNumber, String firstName, String lastName, String middleName, String address, String birthday, char gender, double initialDeposit, int pin) {
        super(accountNumber, firstName, lastName, middleName, address, birthday, gender, "Current Account", initialDeposit, pin);
    }
}

class BankingSystem {
    public static void main(String[] args) {
        Bank bank = new Bank("NewWorldBank");
        Scanner scanner = new Scanner(System.in);
        
        
        System.out.println("\n-----------------------------------");
        animateWelcomeMessage("-----Welcome-to-New-World-Bank----");
        
        System.out.println("-");

       while (true) {
            displayMenu();

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    openAccount(bank, scanner);
                    break;
                case 2:
                    balanceInquiry(bank, scanner);
                    break;
                case 3:
                    deposit(bank, scanner);
                    break;
                case 4:
                    withdraw(bank, scanner);
                    break;
                case 5:
                    viewAccountInformation(bank, scanner);
                    break;
                case 6:
                    // Close Account
                    System.out.println("Enter Account Number: ");
                    int closeAccountNumber = scanner.nextInt();
                    bank.closeAccount(closeAccountNumber);
                    break;

                case 7:
                    exitProgram();
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("-----------------------------------\n");
        System.out.println("1. Open a New Account");
        System.out.println("2. Balance Inquiry");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. View Account Information");
        System.out.println("6. Close Account");
        System.out.println("7. Exit Program\n");
        System.out.print("> Select an option: ");
    }
    
     private static void animateWelcomeMessage(String message) {
    for (char c : message.toCharArray()) {
        System.out.print(c);
        if (c != ' ') {
            try {
                Thread.sleep(100);  // Adjust the delay (in milliseconds) to control the animation speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    System.out.println();  // Move to the next line after the animation
}



    
    private static void openAccount(Bank bank, Scanner scanner) {
        bank.openAccount(scanner);
    }

    private static void balanceInquiry(Bank bank, Scanner scanner) {
        System.out.println("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        Account account = bank.findAccount(accountNumber);
        if (account != null) {
            System.out.println("Current Balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void deposit(Bank bank, Scanner scanner) {
        int depositAccountNumber;
        Account depositAccount;
        
        System.out.println("Enter Account Number: ");
        depositAccountNumber = scanner.nextInt();
        depositAccount = bank.findAccount(depositAccountNumber);
        
        if (depositAccount != null) {
            System.out.println("Enter the deposit amount: ");
            double depositAmount = scanner.nextDouble();
            
            if (depositAmount < 0 || depositAmount < depositAccount.getMinDepositAmount()) {
                System.out.println("The entered amount is not valid. Please enter a valid amount or cancel the transaction.");
            } else {
                System.out.println("Deposit amount: " + depositAmount);
                System.out.println("Confirm deposit? (Type 'yes' to confirm, 'no' to cancel): ");
                String confirmation = scanner.next();
                
                if (confirmation.equalsIgnoreCase("yes")) {
                    depositAccount.deposit(depositAmount);
                    System.out.println("Transaction successful. New balance: " + depositAccount.getBalance());
                } else {
                    System.out.println("Transaction canceled.");
                }
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdraw(Bank bank, Scanner scanner) {
        int withdrawAccountNumber;
        Account withdrawAccount;
        
        System.out.println("Enter Account Number: ");
        withdrawAccountNumber = scanner.nextInt();
        withdrawAccount = bank.findAccount(withdrawAccountNumber);
        
        if (withdrawAccount != null) {
            System.out.println("Enter the withdrawal amount: ");
            double withdrawalAmount = scanner.nextDouble();
            
            if (withdrawalAmount < 0 || withdrawalAmount > withdrawAccount.getBalance() || withdrawalAmount < withdrawAccount.getMinWithdrawAmount()) {
                System.out.println("The entered amount is not valid. Please enter a valid amount or cancel the transaction.");
            } else {
                System.out.println("Withdrawal amount: " + withdrawalAmount);
                System.out.println("Confirm withdrawal? (Type 'yes' to confirm, 'no' to cancel): ");
                String confirmation = scanner.next();
                
                if (confirmation.equalsIgnoreCase("yes")) {
                    withdrawAccount.withdraw(withdrawalAmount);
                    System.out.println("Transaction successful. New balance: " + withdrawAccount.getBalance());
                } else {
                    System.out.println("Transaction canceled.");
                }
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAccountInformation(Bank bank, Scanner scanner) {
        System.out.println("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        Account account = bank.findAccount(accountNumber);
        if (account != null) {
            System.out.println("Account Information:");
            System.out.println("Full Name: " + account.getFullName());
            System.out.println("Address: " + account.getAddress());
            System.out.println("Birthday: " + account.getBirthday());
            System.out.println("Gender: " + account.getGender());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Initial Deposit: " + account.getInitialDeposit());
            System.out.println("Current Balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
    
    private static void exitProgram() {
        System.out.println("Thank you for using NewWorldBank.");
        System.exit(0);
    }
}

