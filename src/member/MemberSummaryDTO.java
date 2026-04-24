package member;



public record MemberSummaryDTO(int id, String firstName, String lastName, String email, MemberStatus status) {
    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName + " - " + status;
    }
}
