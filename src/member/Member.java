package member;

import java.time.LocalDate;

public class Member {
    private int id;
    private MemberStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate membershipDate;
    private String membershipType;

    public Member(int id, String firstName, String lastName, String email, LocalDate membershipDate, String membershipType, String password, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.password = password;
        this.status = MemberStatus.valueOf(status.toUpperCase());
    }

    public Member(String firstName, String lastName, String email, String password, LocalDate membershipDate, String membershipType,  String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.status = MemberStatus.valueOf(status.toUpperCase());
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public String getMembershipType() { return membershipType; }
    public MemberStatus getStatus() { return status; }


    public void setPassword(String password) { this.password = password; }
    public void setStatus(MemberStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName + " – " + status;
    }
}
