package book;

import db.DatabaseConnector;
import util.Searchable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements Searchable<Book> {

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();

        String sql = "SELECT id, title, isbn, year_published, total_copies, available_copies FROM books";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("year_published"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                );
                books.add(book);
            }
        }
        return books;
    }

    public Optional<BookDetailDTO> findById(int id) throws SQLException {
        String sql = """
            SELECT b.id, b.title, b.isbn, b.year_published, b.total_copies, b.available_copies,
                   GROUP_CONCAT(DISTINCT CONCAT(a.first_name, ' ', a.last_name)) AS author_name,
                   GROUP_CONCAT(DISTINCT c.name) AS category_names
            FROM books b
            LEFT JOIN book_authors ba ON b.id = ba.book_id
            LEFT JOIN authors a ON ba.author_id = a.id
            LEFT JOIN book_categories bc ON b.id = bc.book_id
            LEFT JOIN categories c ON bc.category_id = c.id
            WHERE b.id = ?
            GROUP BY b.id
            """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new BookDetailDTO(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("year_published"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies"),
                        rs.getString("author_name") != null ? rs.getString("author_name") : "Unknown",
                        rs.getString("category_names") != null ? rs.getString("category_names") : "None"
                ));
            }
        }
        return Optional.empty();
    }

    public Optional<Book> findBookById(int id) throws SQLException {
        String sql = "SELECT id, title, isbn, year_published, total_copies, available_copies FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("year_published"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                ));
            }
        }
        return Optional.empty();
    }

    public List<Book> findByCategoryId(int categoryId) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = """
            SELECT b.id, b.title, b.isbn, b.year_published, b.total_copies, b.available_copies
            FROM books b
            JOIN book_categories bc ON b.id = bc.book_id
            WHERE bc.category_id = ?
            """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("year_published"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                ));
            }
        }
        return books;
    }

    public void addBookToCategory(int bookId, int categoryId) throws SQLException {
        String sql = "INSERT INTO book_categories (book_id, category_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        }
    }

    public int save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, isbn, year_published, total_copies, available_copies) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getYearPublished());
            stmt.setInt(4, book.getTotalCopies());
            stmt.setInt(5, book.getAvailableCopies());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return -1;
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, isbn=?, year_published=?, total_copies=?, available_copies=? WHERE id=?";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getYearPublished());
            stmt.setInt(4, book.getTotalCopies());
            stmt.setInt(5, book.getAvailableCopies());
            stmt.setInt(6, book.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Uppdaterar antal tillgängliga exemplar för en bok
    // delta = -1 vid utlåning, +1 vid återlämning
    public void updateAvailableCopies(int bookId, int delta) throws SQLException {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }
}
