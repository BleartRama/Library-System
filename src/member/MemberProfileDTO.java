package member;
import java.time.LocalDate;


public record MemberProfileDTO(int id, String firstName, String lastName, String email, LocalDate membershipDate, String membershipType, MemberStatus status) {
    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName +
                "\nEmail: " + email +
                "\nMembership: " + membershipType +
                "\nMember since: " + membershipDate +
                "\nStatus: " + status;
    }
}
