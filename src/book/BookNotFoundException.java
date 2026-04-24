package book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Ingen bok med id " + id + " hittades.");
    }
}