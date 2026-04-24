package loan;

import auth.Role;
import util.ANSI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LoanController {

    private final LoanService loanService = new LoanService();
    private final Scanner scanner = new Scanner(System.in);
    private Role role;
    private int memberId;

    public LoanController(Role role) {
        this.role = role;
    }

    public LoanController(Role role, int memberId) {
        this.role = role;
        this.memberId = memberId;
    }

    public void showLoanMenu() {
        boolean running = true;
        while (running) {
            if (role == Role.LIBRARIAN) {
                System.out.println(ANSI.BOLD + "\n=== Loans ===" + ANSI.RESET);
                System.out.println("1. Show all active loans");
                System.out.println("2. Show loans by member");
                System.out.println("3. Borrow a book");
                System.out.println("4. Return a book");
                System.out.println("0. Back");
            } else {
                System.out.println(ANSI.BOLD + "\n=== Loans ===" + ANSI.RESET);
                System.out.println("1. My loans");
                System.out.println("2. Borrow a book");
                System.out.println("3. Return a book");
                System.out.println("0. Back");
            }

            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (role == Role.LIBRARIAN) {
                switch (choice) {
                    case 1 -> showAllActiveLoans();
                    case 2 -> showLoansByMember();
                    case 3 -> borrowBook();
                    case 4 -> returnBook();
                    case 0 -> running = false;
                    default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
                }
            } else {
                switch (choice) {
                    case 1 -> showLoansByMember();
                    case 2 -> borrowBook();
                    case 3 -> returnBook();
                    case 0 -> running = false;
                    default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
                }
            }
        }
    }

    private void showAllActiveLoans() {
        try {
            List<ActiveLoanDTO> loans = loanService.getAllActiveLoans();
            if (loans.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No active loans." + ANSI.RESET);
            } else {
                loans.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void showLoansByMember() {
        int id;
        if (role == Role.LIBRARIAN) {
            System.out.print("Enter member ID: ");
            id = Integer.parseInt(scanner.nextLine());
        } else {
            id = this.memberId;
        }
        try {
            List<LoanSummaryDTO> loans = loanService.getLoansByMember(id);
            if (loans.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No active loans found." + ANSI.RESET);
            } else {
                loans.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void borrowBook() {
        int id;
        if (role == Role.LIBRARIAN) {
            System.out.print("Enter member ID: ");
            id = Integer.parseInt(scanner.nextLine());
        } else {
            id = this.memberId;
        }
        System.out.print("Enter book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine());
        try {
            loanService.borrowBook(id, bookId);
            System.out.println(ANSI.GREEN + "Book borrowed successfully!" + ANSI.RESET);
        } catch (Exception e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void returnBook() {
        System.out.print("Enter loan ID: ");
        int loanId = Integer.parseInt(scanner.nextLine());
        try {
            loanService.returnBook(loanId);
            System.out.println(ANSI.GREEN + "Book returned successfully!" + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }
}