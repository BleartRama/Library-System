package auth;

import member.Member;
import member.MemberRepository;
import member.MemberStatus;
import java.sql.SQLException;

public class AuthService {
    private MemberRepository memberRepository =  new MemberRepository();
    private Member loggedInMember;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public Member getLoggedInMember() { return loggedInMember; }

    public Role login(String email, String password) throws SQLException {
        if (email.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return Role.LIBRARIAN;
        }



        // Kolla i databasen
        var member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        // Kontrollera lösenordet
        if (!member.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password.");
        }

        // Kontrollera att kontot inte är suspended
        if (member.getStatus() == MemberStatus.SUSPENDED) {
            throw new RuntimeException("Your account is suspended.");
        }

        loggedInMember = member;
        return Role.MEMBER;
    }
}
