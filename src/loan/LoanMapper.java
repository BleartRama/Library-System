// LoanMapper.java
package loan;

// Ansvarar för mappning mellan Loan-entity och DTOs
public class LoanMapper {

    // Omvandlar ett Loan till LoanSummaryDTO — används för låntagarens profilsida
    public static LoanSummaryDTO toSummaryDTO(Loan loan) {
        return new LoanSummaryDTO(
                loan.getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.isOverdue()
        );
    }

    // Omvandlar ett Loan till ActiveLoanDTO — används för bibliotekariens vy
    public static ActiveLoanDTO toActiveLoanDTO(Loan loan) {
        return new ActiveLoanDTO(
                loan.getId(),
                loan.getBook().getTitle(),
                loan.getMember().getFirstName() + " " + loan.getMember().getLastName(),
                loan.getDueDate(),
                loan.isOverdue()
        );
    }
}