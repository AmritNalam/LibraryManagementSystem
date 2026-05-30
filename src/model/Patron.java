
package model;

import java.util.ArrayList;
import java.util.List;

public class Patron extends Person {

    private final List<Book> borrowingHistory =
            new ArrayList<>();

    private final List<Book> currentlyBorrowedBooks =
            new ArrayList<>();

    public Patron(String id,
                  String name) {

        super(id, name);
    }

    public List<Book> getBorrowingHistory() {
        return borrowingHistory;
    }

    public List<Book> getCurrentlyBorrowedBooks() {
        return currentlyBorrowedBooks;
    }

    public void borrowBook(Book book) {

        borrowingHistory.add(book);

        currentlyBorrowedBooks.add(book);
    }

    public void returnBook(Book book) {

        currentlyBorrowedBooks.remove(book);
    }
}
