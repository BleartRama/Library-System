package book;

public class BookMapper {
    public static BookSummaryDTO toSummaryDTO(Book book) {
        return new BookSummaryDTO(
                book.getId(),
                book.getTitle(),
                book.getAvailableCopies()
        );
    }


    public static Book fromFormDTO(BookFormDTO dto) {
        return new Book(
                dto.title(),
                dto.isbn(),
                dto.yearPublished(),
                dto.totalCopies(),
                dto.availableCopies()
        );
    }
}
