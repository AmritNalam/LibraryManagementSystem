package service;

import exception.BookUnavailableException;
import model.Book;
import model.Loan;
import model.Patron;
import util.LibraryLogger;

import java.util.ArrayList;
import java.util.List;

public class LendingService {

    private final List<Loan> loans =
            new ArrayList<>();

    public void checkout(Book book,
                         Patron patron) {

        if (!book.isAvailable()) {

            throw new BookUnavailableException(
                    "Book is already borrowed."
            );
        }

        book.setAvailable(false);

        patron.borrowBook(book);

        loans.add(
                new Loan(
                        book,
                        patron
                )
        );

        LibraryLogger.LOGGER.info(
                "Checkout: "
                        + book.getIsbn()
        );
    }

    public boolean returnBook(Book book,Patron patron) {

        if (book.isAvailable()) {

            return false;
        }

        book.setAvailable(true);

        patron.returnBook(book);

        LibraryLogger.LOGGER.info(
                "Returned: "
                        + book.getIsbn()
        );

        return true;
    }
}