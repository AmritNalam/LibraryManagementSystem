import factory.BookFactory;
import model.Book;
import model.Patron;
import observer.PatronObserver;
import observer.ReservationNotifier;
import repository.BookRepository;
import repository.InMemoryBookRepository;
import repository.InMemoryPatronRepository;
import repository.PatronRepository;
import service.BookService;
import service.LendingService;
import service.PatronService;
import service.RecommendationService;
import service.ReservationService;
import strategy.AuthorSearchStrategy;
import strategy.ISBNSearchStrategy;
import strategy.TitleSearchStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final BookRepository bookRepo =
            new InMemoryBookRepository();

    private static final PatronRepository patronRepo =
            new InMemoryPatronRepository();

    private static final BookService bookService =
            new BookService(bookRepo);

    private static final PatronService patronService =
            new PatronService(patronRepo);

    private static final LendingService lendingService =
            new LendingService();

    private static final ReservationNotifier notifier =
            new ReservationNotifier();

    private static final ReservationService reservationService =
            new ReservationService(notifier);

    private static final RecommendationService recommendationService =
            new RecommendationService();

    public static void main(String[] args) {

        notifier.register(
                new PatronObserver("Library Notification")
        );

        while (true) {

            printMenu();

            int choice = getInt();

            switch (choice) {

                case 1:
                    addBook();
                    break;

                case 2:
                    removeBook();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    displayBooks();
                    break;

                case 5:
                    registerPatron();
                    break;

                case 6:
                    displayPatrons();
                    break;

                case 7:
                    checkoutBook();
                    break;

                case 8:
                    returnBook();
                    break;

                case 9:
                    reserveBook();
                    break;

                case 10:
                    recommendations();
                    break;

                case 11:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {

        System.out.println("\n========================");
        System.out.println("Library Management");
        System.out.println("========================");

        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Search Book");
        System.out.println("4. Display Books");
        System.out.println("5. Register Patron");
        System.out.println("6. Display Patrons");
        System.out.println("7. Checkout Book");
        System.out.println("8. Return Book");
        System.out.println("9. Reserve Book");
        System.out.println("10. Recommendations");
        System.out.println("11. Exit");

        System.out.print("Choice: ");
    }

    private static int getInt() {

        while (!scanner.hasNextInt()) {
            System.out.println("Enter a valid number.");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }

    private static void addBook() {

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Author: ");
        String author = scanner.nextLine();

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Publication Year: ");
        int year = getInt();

        Book book =
                BookFactory.createBook(
                        title,
                        author,
                        isbn,
                        year
                );

        bookService.add(book);

        System.out.println("Book added.");
    }

    private static void removeBook() {

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        bookService.remove(isbn);

        System.out.println("Book removed.");
    }

    private static void searchBook() {

        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. ISBN");

        int option = getInt();

        System.out.print("Keyword: ");
        String keyword = scanner.nextLine();

        List<Book> books;

        switch (option) {

            case 1:
                books =
                        bookService.search(
                                new TitleSearchStrategy(),
                                keyword
                        );
                break;

            case 2:
                books =
                        bookService.search(
                                new AuthorSearchStrategy(),
                                keyword
                        );
                break;

            case 3:
                books =
                        bookService.search(
                                new ISBNSearchStrategy(),
                                keyword
                        );
                break;

            default:
                System.out.println("Invalid option.");
                return;
        }

        if (books.isEmpty()) {

            System.out.println("No books found.");
            return;
        }

        for (Book b : books) {

            System.out.println(
                    b.getTitle()
                            + " | "
                            + b.getAuthor()
                            + " | "
                            + b.getIsbn()
            );
        }
    }

    private static void displayBooks() {

        Collection<Book> books =
                bookService.getAll();

        if (books.isEmpty()) {

            System.out.println("No books available.");
            return;
        }

        for (Book b : books) {

            System.out.println(
                    b.getTitle()
                            + " | "
                            + b.getAuthor()
                            + " | "
                            + b.getIsbn()
                            + " | "
                            + (b.isAvailable()
                            ? "Available"
                            : "Borrowed")
            );
        }
    }

    private static void registerPatron() {

        System.out.print("Patron ID: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        Patron patron =
                new Patron(id, name);

        patronService.add(patron);

        System.out.println("Patron registered.");
    }

    private static void displayPatrons() {

        Collection<Patron> patrons =
                patronRepo.findAll();

        if (patrons.isEmpty()) {

            System.out.println(
                    "No patrons."
            );

            return;
        }

        for (Patron patron :
                patrons) {

            System.out.println(
                    "\n================="
            );

            System.out.println(
                    "Patron ID : "
                            + patron.getId()
            );

            System.out.println(
                    "Name : "
                            + patron.getName()
            );

            System.out.println(
                    "\nCurrently Borrowed Books:"
            );

            if (patron
                    .getCurrentlyBorrowedBooks()
                    .isEmpty()) {

                System.out.println(
                        "None"
                );
            }
            else {

                for (Book book :
                        patron.getCurrentlyBorrowedBooks()) {

                    System.out.println(
                            "- "
                                    + book.getTitle()
                                    + " ("
                                    + book.getIsbn()
                                    + ")"
                    );
                }
            }

            System.out.println(
                    "\nBorrowing History:"
            );

            if (patron
                    .getBorrowingHistory()
                    .isEmpty()) {

                System.out.println(
                        "None"
                );
            }
            else {

                for (Book book :
                        patron.getBorrowingHistory()) {

                    System.out.println(
                            "- "
                                    + book.getTitle()
                                    + " ("
                                    + book.getIsbn()
                                    + ")"
                    );
                }
            }
        }
    }
    private static void checkoutBook() {

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Patron ID: ");
        String patronId = scanner.nextLine();

        Book book = bookRepo
                .findByIsbn(isbn)
                .orElse(null);

        Patron patron = patronRepo
                .findById(patronId)
                .orElse(null);

        if (book == null) {

            System.out.println("Book not found.");
            return;
        }

        if (patron == null) {

            System.out.println("Patron not found.");
            return;
        }

        try {

            lendingService.checkout(
                    book,
                    patron
            );

            System.out.println(
                    "Book issued successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Checkout failed: "
                            + e.getMessage()
            );
        }
    }

    private static void returnBook() {

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Book book = bookRepo
                .findByIsbn(isbn)
                .orElse(null);

        if (book == null) {

            System.out.println("Book not found.");
            return;
        }

        if (book.isAvailable()) {

            System.out.println(
                    "Book is already in library."
            );

            return;
        }

        Patron holder = null;

        for (Patron patron :
                patronRepo.findAll()) {

            if (patron
                    .getCurrentlyBorrowedBooks()
                    .contains(book)) {

                holder = patron;
                break;
            }
        }

        if (holder == null) {

            System.out.println(
                    "Current borrower not found."
            );

            return;
        }

        boolean returned =
                lendingService.returnBook(
                        book,
                        holder
                );

        if (returned) {

            reservationService.onReturn(isbn);

            System.out.println(
                    "Book returned successfully."
            );
        }
        else {

            System.out.println(
                    "Book already returned."
            );
        }
    }

    private static void reserveBook() {

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Patron ID: ");
        String patronId = scanner.nextLine();

        Patron patron = patronRepo
                .findById(patronId)
                .orElse(null);

        if (patron == null) {

            System.out.println("Patron not found.");
            return;
        }

        Book book =
                bookRepo.findByIsbn(isbn)
                        .orElse(null);

        if (book == null) {

            System.out.println(
                    "Book not found."
            );

            return;
        }

        boolean success =
                reservationService.reserve(
                        book,
                        patron
                );

        if (success) {

            System.out.println(
                    "Reservation added."
            );
        }
        else {

            System.out.println(
                    "Book is already available. No reservation needed."
            );
        }

        System.out.println("Reservation added.");
    }

    private static void recommendations() {

        System.out.print("Patron ID: ");
        String patronId = scanner.nextLine();

        Patron patron =
                patronRepo.findById(
                        patronId
                ).orElse(null);

        if (patron == null) {

            System.out.println("Patron not found.");
            return;
        }

        List<Book> books =
                recommendationService.recommend(
                        patron,
                        bookService.getAll()
                );

        if (books.isEmpty()) {

            System.out.println(
                    "No recommendations."
            );

            return;
        }

        System.out.println(
                "Recommended Books:"
        );

        for (Book b : books) {

            System.out.println(
                    b.getTitle()
                            + " - "
                            + b.getAuthor()
            );
        }
    }
}