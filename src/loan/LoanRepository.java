package loan;

import book.Book;
import db.DatabaseConnector;
import member.Member;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository {

    // Hämtar alla aktiva lån med JOIN mot books och members
    public List<Loan> findActiveLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = """
                SELECT l.id, l.book_id, l.member_id, l.loan_date, l.due_date, l.return_date, l.status,
                       b.title, b.isbn, b.year_published, b.total_copies, b.available_copies,
                       m.first_name, m.last_name, m.email
                FROM loans l
                JOIN books b ON l.book_id = b.id
                JOIN members m ON l.member_id = m.id
                WHERE l.status = 'active'
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                loans.add(mapToLoan(rs)); // mappar varje rad till ett Loan-objekt
            }
        }
        return loans;
    }

    public Optional<Loan> findById(int loanId) throws SQLException {

        String sql = """
                SELECT l.id, l.book_id, l.member_id, l.loan_date, l.due_date, l.return_date, l.status,
                       b.title, b.isbn, b.year_published, b.total_copies, b.available_copies,
                       m.first_name, m.last_name, m.email
                FROM loans l
                JOIN books b ON l.book_id = b.id
                JOIN members m ON l.member_id = m.id
                WHERE l.id = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToLoan(rs));
            }
        }
        return Optional.empty();
    }

    // Hämtar aktiva lån för en specifik medlem
    public List<Loan> findByMemberId(int memberId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = """
                SELECT l.id, l.book_id, l.member_id, l.loan_date, l.due_date, l.return_date, l.status,
                       b.title, b.isbn, b.year_published, b.total_copies, b.available_copies,
                       m.first_name, m.last_name, m.email
                FROM loans l
                JOIN books b ON l.book_id = b.id
                JOIN members m ON l.member_id = m.id
                WHERE l.member_id = ? AND l.status = 'active'
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapToLoan(rs));
            }
        }
        return loans;
    }

    // Sparar ett nytt lån i databasen
    public void save(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (book_id, member_id, loan_date, due_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getBookId());
            stmt.setInt(2, loan.getMemberId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setString(5, loan.getStatus().name().toLowerCase());
            stmt.executeUpdate();
        }
    }

    // Markerar ett lån som återlämnat och sätter return_date till idag
    public void returnLoan(int loanId) throws SQLException {
        String sql = "UPDATE loans SET return_date = ?, status = 'returned' WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, loanId);
            stmt.executeUpdate();
        }
    }

    // Privat hjälpmetod — bygger ett Loan-objekt från en rad i ResultSet
    // Används av findActiveLoans() och findByMemberId() för att undvika duplicerad kod
    private Loan mapToLoan(ResultSet rs) throws SQLException {

        // Bygger ett Book-objekt från JOIN-data
        Book book = new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("isbn"),
                rs.getInt("year_published"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );

        // Bygger ett Member-objekt med bara de fält som finns i JOIN
        // membershipDate, membershipType och password hämtas inte här
        Member member = new Member(
                rs.getInt("member_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                null,
                null,
                null,
                "active"
        );

        // return_date kan vara NULL i databasen om boken inte är återlämnad
        LocalDate returnDate = rs.getDate("return_date") != null
                ? rs.getDate("return_date").toLocalDate()
                : null;

        return new Loan(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                book,
                member,
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate,
                LoanStatus.valueOf(rs.getString("status").toUpperCase())
        );
    }


}