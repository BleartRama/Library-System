package book;

public record BookDetailDTO(int id, String title, String isbn, int yearPublished,
                            int totalCopies, int availableCopies,
                            String authorName, String categoryNames) {
    @Override
    public String toString() {
        return "ID: " + id +
                "\nTitle: " + title +
                "\nAuthor: " + authorName +
                "\nCategories: " + categoryNames +
                "\nISBN: " + isbn +
                "\nYear: " + yearPublished +
                "\nTotal copies: " + totalCopies +
                "\nAvailable: " + availableCopies;
    }
}
