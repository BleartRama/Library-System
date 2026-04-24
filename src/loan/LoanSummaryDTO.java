// LoanSummaryDTO.java
package loan;

import java.time.LocalDate;

// DTO för låntagarens vy över sina egna lån
public record LoanSummaryDTO(int id, String bookTitle, LocalDate loanDate, LocalDate dueDate, boolean isOverdue) {
    @Override
    public String toString() {
        return "[" + id + "] " + bookTitle + " (due: " + dueDate + ")" + (isOverdue ? " – OVERDUE" : "");
    }
}
