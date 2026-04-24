package author;

import db.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    // Hämtar alla författare
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, nationality, birth_date FROM authors";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                authors.add(new Author(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("nationality"),
                        rs.getDate("birth_date").toLocalDate()
                ));
            }
        }
        return authors;
    }

    // Sparar en ny författare
    public void save(Author author) throws SQLException {
        String sql = "INSERT INTO authors (first_name, last_name, nationality, birth_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getNationality());
            stmt.setDate(4, Date.valueOf(author.getBirthDate()));
            stmt.executeUpdate();
        }
    }
}