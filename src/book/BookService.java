package book;

import java.sql.SQLException;
import java.util.List;

public class BookService {

    private final BookRepository bookRepository = new BookRepository();

    public List<BookSummaryDTO> getAllBooks() throws SQLException {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookMapper::toSummaryDTO)
                .toList();
    }

    public BookDetailDTO getBookById(int id) throws SQLException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public int addBook(BookFormDTO dto) throws SQLException {
        Book book = BookMapper.fromFormDTO(dto);
        return bookRepository.save(book);
    }

    public void deleteBook(int id) throws SQLException {
        bookRepository.delete(id);
    }

    public List<Book> getBooksByCategory(int categoryId) throws SQLException {
        return bookRepository.findByCategoryId(categoryId);
    }

    // I BookService
    public void addBookToCategory(int bookId, int categoryId) throws SQLException {
        bookRepository.addBookToCategory(bookId, categoryId);
    }
}
