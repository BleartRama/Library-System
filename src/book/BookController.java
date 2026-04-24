package book;

import auth.Role;
import author.Author;
import author.AuthorRepository;
import category.CategoryRepository;
import util.ANSI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class BookController {
    private final BookService bookService = new BookService();
    private Role role;
    private final AuthorRepository authorRepository = new AuthorRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final Scanner scanner = new Scanner(System.in);

    public void showBookMenu() {
        boolean running = true;
        while (running) {
            System.out.println(ANSI.BOLD + "\n=== Books ===" + ANSI.RESET);
            System.out.println("1. Show all books");
            System.out.println("2. Show book details");
            System.out.println("3. View categories");
            if (role == Role.LIBRARIAN) {
                System.out.println("4. Add book");
                System.out.println("5. Delete book");
                System.out.println("6. Add author");
            }
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> showAllBooks();
                case 2 -> showBookDetails();
                case 3 -> viewCategories();
                case 4 -> { if (role == Role.LIBRARIAN) addBook(); else System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET); }
                case 5 -> { if (role == Role.LIBRARIAN) deleteBook(); else System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET); }
                case 6 -> { if (role == Role.LIBRARIAN) addAuthor(); else System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET); }
                case 0 -> running = false;
                default -> System.out.println(ANSI.RED + "Invalid choice" + ANSI.RESET);
            }
        }
    }

    private void showAllBooks() {
        try {
            List<BookSummaryDTO> books = bookService.getAllBooks();

            if (books.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No books found" + ANSI.RESET);
            } else {
                for (BookSummaryDTO book : books) {
                    System.out.println(book);
                }
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void showBookDetails() {
        System.out.print("Enter book ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            BookDetailDTO book = bookService.getBookById(id);
            System.out.println(ANSI.CYAN + book + ANSI.RESET);
        } catch (BookNotFoundException e) {
            System.out.println(ANSI.RED + e.getMessage() + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter year published: ");
        int yearPublished = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter total copies: ");
        int totalCopies = Integer.parseInt(scanner.nextLine());

        BookFormDTO dto = new BookFormDTO(title, isbn, yearPublished, totalCopies, totalCopies);
        try {
            int newBookId = bookService.addBook(dto);
            System.out.println(ANSI.GREEN + "Book added with ID: " + newBookId + ANSI.RESET);

            List<category.Category> categories = categoryRepository.findAll();
            categories.forEach(System.out::println);
            System.out.print("Enter category ID (0 to skip): ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            if (categoryId != 0) {
                bookService.addBookToCategory(newBookId, categoryId);
                System.out.println(ANSI.GREEN + "Book added to category!" + ANSI.RESET);
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void deleteBook() {
        System.out.print("Enter book ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            bookService.deleteBook(id);
            System.out.println(ANSI.GREEN + "Book deleted successfully!" + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void addAuthor() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter nationality: ");
        String nationality = scanner.nextLine();
        System.out.print("Enter birth date (yyyy-mm-dd): ");
        String birthDateStr = scanner.nextLine();

        try {
            Author author = new Author(firstName, lastName, nationality,
                    java.time.LocalDate.parse(birthDateStr));
            authorRepository.save(author);
            System.out.println(ANSI.GREEN + "Author added successfully!" + ANSI.RESET);
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private void viewCategories() {
        try {
            List<category.Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No categories found." + ANSI.RESET);
                return;
            }
            categories.forEach(System.out::println);

            System.out.print("Enter category ID to view books (0 to go back): ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            if (categoryId == 0) return;

            List<Book> books = bookService.getBooksByCategory(categoryId);
            if (books.isEmpty()) {
                System.out.println(ANSI.YELLOW + "No books found in this category." + ANSI.RESET);
            } else {
                books.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println(ANSI.RED + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    public BookController(Role role) {
        this.role = role;
    }
}