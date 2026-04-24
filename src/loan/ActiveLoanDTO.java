// ActiveLoanDTO.java
package loan;

import java.time.LocalDate;

// DTO för bibliotekariens vy över alla aktiva lån
// Innehåller både bokens titel och medlemmens namn
public record ActiveLoanDTO(int id, String bookTitle, String memberName, LocalDate dueDate, boolean isOverdue) {
    @Override
    public String toString() {
        return "[" + id + "] " + bookTitle + " – " + memberName + " (due: " + dueDate + ")" + (isOverdue ? " – OVERDUE" : "");
    }
}
