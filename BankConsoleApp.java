import java.util.HashMap;
import java.util.Scanner;

class Account {

    int id;
    String name;
    double balance;

    Account(int id, String name) {
        this.id = id;
        this.name = name;
        balance = 0;
    }
}

class AccountNotFoundException extends Exception {}

class InsufficientFundsException extends Exception {}

public class BankConsoleApp {

    static HashMap<Integer, Account> accounts = new HashMap<>();
    static int nextId = 1;

    static int createAccount(String name) {

        Account acc = new Account(nextId, name);
        accounts.put(nextId, acc);

        return nextId++;
    }

    static void deposit(int id, double amount)
            throws AccountNotFoundException {

        if (!accounts.containsKey(id))
            throw new AccountNotFoundException();

        accounts.get(id).balance += amount;
    }

    static void withdraw(int id, double amount)
            throws AccountNotFoundException,
            InsufficientFundsException {

        if (!accounts.containsKey(id))
            throw new AccountNotFoundException();

        Account acc = accounts.get(id);

        if (acc.balance < amount)
            throw new InsufficientFundsException();

        acc.balance -= amount;
    }

    static double checkBalance(int id)
            throws AccountNotFoundException {

        if (!accounts.containsKey(id))
            throw new AccountNotFoundException();

        return accounts.get(id).balance;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            try {

                switch (choice) {

                    case 1:

                        sc.nextLine();

                        System.out.print("Enter name: ");
                        String name = sc.nextLine();

                        int id = createAccount(name);

                        System.out.println("Account created");
                        System.out.println("ID: " + id);

                        break;

                    case 2:

                        System.out.print("Enter ID: ");
                        int id1 = sc.nextInt();

                        System.out.print("Enter amount: ");
                        double amount1 = sc.nextDouble();

                        deposit(id1, amount1);

                        System.out.println("Money deposited");

                        break;

                    case 3:

                        System.out.print("Enter ID: ");
                        int id2 = sc.nextInt();

                        System.out.print("Enter amount: ");
                        double amount2 = sc.nextDouble();

                        withdraw(id2, amount2);

                        System.out.println("Money withdrawn");

                        break;

                    case 4:

                        System.out.print("Enter ID: ");
                        int id3 = sc.nextInt();

                        System.out.println("Balance: " + checkBalance(id3));

                        break;

                    case 5:

                        System.out.println("Thank you");
                        return;

                    default:

                        System.out.println("Wrong choice");
                }

            } catch (AccountNotFoundException e) {

                System.out.println("Account not found");

            } catch (InsufficientFundsException e) {

                System.out.println("Not enough balance");
            }
        }
    }
}