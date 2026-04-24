package member;

import util.ANSI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class MemberController {
    private final MemberService memberService = new MemberService();
    private final Scanner scanner = new Scanner(System.in);

    public void showMemberMenu() {
        boolean running = true;
        while (running) {
            System.out.println(ANSI.BOLD + "\n=== Member Menu ===" + ANSI.RESET);
            System.out.println("1. Show all members");
            System.out.println("2. Show member profile");
            System.out.println("3. Create a new member");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> showAllMembers();
                case 2 -> showMemberProfile();
                case 3 -> createMember();
                case 0 -> running = false;
                default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
            }
        }
    }

    private void showAllMembers() {
        try {
            List<MemberSummaryDTO> members = memberService.getAllMembers();

            if (members.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No members found" + ANSI.RESET);
            } else {
                for (MemberSummaryDTO member : members) {
                    System.out.println(member);
                }
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void showMemberProfile() {
        System.out.print("Enter member ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            MemberProfileDTO member = memberService.getMemberProfile(id);
            System.out.println(ANSI.CYAN + member + ANSI.RESET);
        } catch (MemberNotFoundException e) {
            System.out.println(ANSI.RED + e.getMessage() + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void createMember() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Membership Type (standard/premium): ");
        String membershipType = scanner.nextLine();

        try {
            memberService.createMember(firstName, lastName, email, password, membershipType);
            System.out.println(ANSI.GREEN + "Member created successfully!" + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }
}