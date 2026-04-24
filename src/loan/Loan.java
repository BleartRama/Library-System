package loan;



import book.Book;
import member.Member;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private Book book;
    private Member member;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    public Loan(int id, int bookId, int memberId, Book book, Member member, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, LoanStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.book = book;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Loan( int bookId, int memberId, Book book, Member member, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, LoanStatus status) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.book = book;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }



    public int getId()              { return id; }
    public Book getBook()           { return book; }
    public Member getMember()       { return member; }
    public LocalDate getLoanDate()  { return loanDate; }
    public LocalDate getDueDate()   { return dueDate; }
    public LocalDate getReturnDate(){ return returnDate; }
    public LoanStatus getStatus()   { return status; }

    public int getBookId()   { return book != null ? book.getId() : bookId; }
    public int getMemberId() { return member != null ? member.getId() : memberId; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(LoanStatus status)        { this.status = status; }

    public boolean isReturned() { return returnDate != null; }
    public boolean isOverdue()  { return !isReturned() && LocalDate.now().isAfter(dueDate); }

    @Override
    public String toString() {
        return "[" + id + "] " + book.getTitle() + " – " + member.getFirstName() + " " + member.getLastName() + " (due: " + dueDate + ") – " + status;
    }
}

