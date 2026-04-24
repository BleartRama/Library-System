package member;


public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(int id) {
        super("No member with id " + id + " found.");
    }
}
