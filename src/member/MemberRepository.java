package member;


import db.DatabaseConnector;
import util.Searchable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class MemberRepository implements Searchable<Member> {
    public List<Member> findAll() throws SQLException {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT id, first_name, last_name, email, membership_date, membership_type, password, status FROM members";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("membership_date").toLocalDate(),
                        rs.getString("membership_type"),
                        rs.getString("password"),
                        rs.getString("status")

                );
                members.add(member);
            }
        }
        return members;
    }

    public Optional<Member> findById(int id) throws SQLException {
        String sql = "SELECT * FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("membership_date").toLocalDate(),
                        rs.getString("membership_type"),
                        rs.getString("password"),
                        rs.getString("status")
                );
                return Optional.of(member);
            }

        }
        return Optional.empty();
    }

    public void save(Member member) throws SQLException {
        String sql = "INSERT INTO members (first_name, last_name, email, membership_date, membership_type, password, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setDate(4, Date.valueOf(member.getMembershipDate()));
            stmt.setString(5, member.getMembershipType());
            stmt.setString(6, member.getPassword());
            stmt.setString(7, member.getStatus().name().toLowerCase());
            stmt.executeUpdate();
        }
    }

    public Optional<Member> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM members WHERE email = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("membership_date").toLocalDate(),
                        rs.getString("membership_type"),
                        rs.getString("password"),
                        rs.getString("status")
                );
                return Optional.of(member);
            }

        }
        return Optional.empty();
    }


}
