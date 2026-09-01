import java.util.*;
class Account {
    int id;
    String name;
    double balance;
    List<Transaction> transactions = new ArrayList<>();
    Account(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
class Transaction {
    String type;
    double amount;
    Account from, to;
    Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }
    Transaction(String type, double amount, Account from, Account to) {
        this.type = type;
        this.amount = amount;
        this.from = from;
        this.to = to;
    }
}
class AccountNotFoundException extends Exception {}
class InsufficientFundsException extends Exception {}
public class BankConsoleApp {
    static HashMap<Integer, Account> accounts = new HashMap<>();
    static HashMap<String, List<Integer>> customerAccounts = new HashMap<>();
    static int nextId = 1;

    static Account getAccount(int id) throws AccountNotFoundException {
        Account a = accounts.get(id);
        if (a == null) throw new AccountNotFoundException();
        return a;
    }

    static int createAccount(String name) {
        Account a = new Account(nextId, name);
        accounts.put(nextId, a);
        customerAccounts.computeIfAbsent(name, x -> new ArrayList<>()).add(nextId);
        return nextId++;
    }

    static void deposit(int id, double amount)
            throws AccountNotFoundException {
        if (amount <= 0) return;
        Account a = getAccount(id);
        a.balance += amount;
        a.transactions.add(new Transaction("Deposit", amount));
    }

    static void withdraw(int id, double amount)
            throws AccountNotFoundException, InsufficientFundsException {
        if (amount <= 0) return;
        Account a = getAccount(id);
        if (a.balance < amount) throw new InsufficientFundsException();
        a.balance -= amount;
        a.transactions.add(new Transaction("Withdraw", amount));
    }

    static double checkBalance(int id) throws AccountNotFoundException {
        return getAccount(id).balance;
    }

    static void closeAccount(int id) throws AccountNotFoundException {
        Account a = getAccount(id);
        accounts.remove(id);
        customerAccounts.get(a.name).remove(Integer.valueOf(id));
    }

    static void transfer(int fromId, int toId, double amount)
            throws AccountNotFoundException, InsufficientFundsException {

        if (amount <= 0) return;

        Account from = getAccount(fromId);
        Account to = getAccount(toId);

        if (from.balance < amount) throw new InsufficientFundsException();

        from.balance -= amount;
        try {
            to.balance += amount;
            Transaction t = new Transaction("Transfer", amount, from, to);
            from.transactions.add(t);
            to.transactions.add(t);
        } catch (Exception e) {
            from.balance += amount;
            throw e;
        }
    }

    static void reverse(int id) throws AccountNotFoundException {
        Account a = getAccount(id);

        if (a.transactions.isEmpty()) {
            System.out.println("No transaction");
            return;
        }

        Transaction t = a.transactions.remove(a.transactions.size() - 1);

        if (t.type.equals("Deposit"))
            a.balance -= t.amount;

        else if (t.type.equals("Withdraw"))
            a.balance += t.amount;

        else {
            t.from.balance += t.amount;
            t.to.balance -= t.amount;
            t.from.transactions.remove(t);
            t.to.transactions.remove(t);
        }

        System.out.println("Transaction reversed");
    }

    static void findAccounts(String name) {
        System.out.println(customerAccounts.getOrDefault(name,
                new ArrayList<>()));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Create 2.Deposit 3.Withdraw 4.Balance");
            System.out.println("5.Close 6.Transfer 7.Reverse 8.Find 9.Exit");
            int ch = sc.nextInt();

            try {
                switch (ch) {
                    case 1:
                        sc.nextLine();
                        System.out.print("Name: ");
                        System.out.println("ID: " + createAccount(sc.nextLine()));
                        break;

                    case 2:
                        System.out.print("ID: ");
                        int d = sc.nextInt();
                        System.out.print("Amount: ");
                        deposit(d, sc.nextDouble());
                        System.out.println("Deposited");
                        break;

                    case 3:
                        System.out.print("ID: ");
                        int w = sc.nextInt();
                        System.out.print("Amount: ");
                        withdraw(w, sc.nextDouble());
                        System.out.println("Withdrawn");
                        break;

                    case 4:
                        System.out.print("ID: ");
                        System.out.println("Balance: " + checkBalance(sc.nextInt()));
                        break;

                    case 5:
                        System.out.print("ID: ");
                        closeAccount(sc.nextInt());
                        System.out.println("Account closed");
                        break;

                    case 6:
                        System.out.print("From ID: ");
                        int from = sc.nextInt();
                        System.out.print("To ID: ");
                        int to = sc.nextInt();
                        System.out.print("Amount: ");
                        transfer(from, to, sc.nextDouble());
                        System.out.println("Transferred");
                        break;

                    case 7:
                        System.out.print("ID: ");
                        reverse(sc.nextInt());
                        break;

                    case 8:
                        sc.nextLine();
                        System.out.print("Name: ");
                        findAccounts(sc.nextLine());
                        break;

                    case 9:
                        return;

                    default:
                        System.out.println("Invalid choice");
                }

            } catch (AccountNotFoundException e) {
                System.out.println("Account not found");
            } catch (InsufficientFundsException e) {
                System.out.println("Not enough balance");
            }
        }
    }
}
