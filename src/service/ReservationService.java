package service;

import model.Book;
import model.Patron;
import observer.ReservationNotifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ReservationService {

    private final Map<String,
            Queue<Patron>> reservations =
            new HashMap<>();

    private final ReservationNotifier notifier;

    public ReservationService(
            ReservationNotifier notifier) {

        this.notifier = notifier;
    }

    public boolean reserve(
            Book book,
            Patron patron) {

        if (book.isAvailable()) {

            return false;
        }

        reservations
                .computeIfAbsent(
                        book.getIsbn(),
                        k -> new LinkedList<>()
                )
                .offer(patron);

        return true;
    }

    public void onReturn(
            String isbn) {

        Queue<Patron> queue =
                reservations.get(isbn);

        if (queue == null
                || queue.isEmpty()) {

            return;
        }

        Patron nextPatron =
                queue.poll();

        notifier.notifyUsers(
                "Book "
                        + isbn
                        + " is now available for "
                        + nextPatron.getName()
        );
    }
}