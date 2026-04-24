// LoanService.java
package loan;

import book.BookRepository;
import member.MemberRepository;
import member.MemberStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Affärslogik för lån — kontrollerar regler innan ett lån skapas
public class LoanService {

    private final LoanRepository loanRepository = new LoanRepository();
    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();

    // Hämtar alla aktiva lån — bibliotekariens vy
    public List<ActiveLoanDTO> getAllActiveLoans() throws SQLException {
        List<Loan> loans = loanRepository.findActiveLoans();
        return loans.stream()
                .map(LoanMapper::toActiveLoanDTO)
                .toList();
    }

    // Hämtar aktiva lån för en specifik medlem — låntagarens profilsida
    public List<LoanSummaryDTO> getLoansByMember(int memberId) throws SQLException {
        List<Loan> loans = loanRepository.findByMemberId(memberId);
        return loans.stream()
                .map(LoanMapper::toSummaryDTO)
                .toList();
    }

    // Lånar en bok — kontrollerar affärsregler innan lån skapas
    public void borrowBook(int memberId, int bookId) throws SQLException {

        // Kontrollera att medlemmen finns
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found."));

        // Kontrollera att medlemmen inte är suspended
        if (member.getStatus() == MemberStatus.SUSPENDED) {
            throw new RuntimeException("Member is suspended and cannot borrow books.");
        }

        // Kontrollera att boken finns
        var book = bookRepository.findBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found."));

        // Kontrollera att det finns tillgängliga exemplar
        if (book.getAvailableCopies() == 0) {
            throw new RuntimeException("No available copies of this book.");
        }

        // Skapa lånet — due_date sätts till 2 veckor framåt
        Loan loan = new Loan(
                bookId, memberId, book, member,
                LocalDate.now(),
                LocalDate.now().plusWeeks(2),
                null,
                LoanStatus.ACTIVE
        );

        // Spara lånet och minska available_copies med 1
        loanRepository.save(loan);
        bookRepository.updateAvailableCopies(bookId, -1);
    }

    // Återlämnar en bok — sätter return_date och ökar available_copies med 1
    public void returnBook(int loanId) throws SQLException {

        // Hämta lånet för att få book_id
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found."));

        // Markera lånet som återlämnat i databasen
        loanRepository.returnLoan(loanId);

        // Öka available_copies med 1 nu när boken är tillbaka
        bookRepository.updateAvailableCopies(loan.getBookId(), 1);
    }
}