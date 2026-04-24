import auth.AuthService;
import auth.Role;
import book.BookController;
import loan.LoanController;
import member.MemberController;
import util.ANSI;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean appRunning = true;

        while (appRunning) {
            AuthService authService = new AuthService();

            System.out.println(ANSI.BOLD + ANSI.CYAN + "\n=== Library System ===" + ANSI.RESET);
            System.out.println("1. Login as librarian");
            System.out.println("2. Login as member");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int loginChoice = Integer.parseInt(scanner.nextLine());
            if (loginChoice == 0) {
                appRunning = false;
                continue;
            }

            Role role;
            try {
                if (loginChoice == 1) {
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    role = authService.login("admin", password);
                } else {
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    role = authService.login(email, password);
                }
            } catch (RuntimeException | SQLException e) {
                System.out.println(ANSI.RED + "Login failed: " + e.getMessage() + ANSI.RESET);
                continue;
            }

            System.out.println(ANSI.GREEN + "Login successful!" + ANSI.RESET);

            BookController bookController = new BookController(role);
            MemberController memberController = new MemberController();
            int memberId = role == Role.MEMBER ? authService.getLoggedInMember().getId() : -1;
            LoanController loanController = new LoanController(role, memberId);

            boolean loggedIn = true;
            while (loggedIn) {
                if (role == Role.LIBRARIAN) {
                    System.out.println(ANSI.BOLD + ANSI.CYAN + "\n=== Library System [LIBRARIAN] ===" + ANSI.RESET);
                    System.out.println("1. Books");
                    System.out.println("2. Members");
                    System.out.println("3. Loans");
                    System.out.println("0. Logout");
                } else {
                    System.out.println(ANSI.BOLD + ANSI.CYAN + "\n=== Library System [MEMBER] ===" + ANSI.RESET);
                    System.out.println("1. Books");
                    System.out.println("2. My loans");
                    System.out.println("0. Logout");
                }

                System.out.print("Choose: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (role == Role.LIBRARIAN) {
                    switch (choice) {
                        case 1 -> bookController.showBookMenu();
                        case 2 -> memberController.showMemberMenu();
                        case 3 -> loanController.showLoanMenu();
                        case 0 -> {
                            System.out.println(ANSI.YELLOW + "Logged out successfully." + ANSI.RESET);
                            loggedIn = false;
                        }
                        default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
                    }
                } else {
                    switch (choice) {
                        case 1 -> bookController.showBookMenu();
                        case 2 -> loanController.showLoanMenu();
                        case 0 -> {
                            System.out.println(ANSI.YELLOW + "Logged out successfully." + ANSI.RESET);
                            loggedIn = false;
                        }
                        default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
                    }
                }
            }
        }

        System.out.println(ANSI.BOLD + "Goodbye!" + ANSI.RESET);
    }
}