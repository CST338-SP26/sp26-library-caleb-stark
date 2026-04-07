import java.time.LocalDate;

/**
 * This is the main 'driver' it is used to run the files and get the output seen in the prompt.
 * @author Drew "Dr.C" Clinkenbeard
 */
public class Main {
    public static void main(String[] args) {

        Library csumb = new Library("CSUMB");
        csumb.init("Library01.csv");
        int numb = csumb.listBooks();
        System.out.println("total books: " + numb);
        numb = csumb.listReaders();

        System.out.println("total readers: " + numb );
        csumb.listShelves(true);

        Book bestServedCold = new Book("12345","Best Served Cold","GrimDark",235,"Joe Abercrombie",LocalDate.now());
        Shelf scifi = csumb.getShelf("sci-fi");
        Shelf romance = csumb.getShelf(4);
        romance = csumb.getShelf("Romance");
        System.out.println(romance);
        csumb.addShelf("Romance");
        romance = csumb.getShelf("Romance");
        System.out.println(romance);
        scifi.addBook(bestServedCold);
        csumb.addBook(bestServedCold);
        csumb.addShelf("GrimDark");
        System.out.println("Done");
        csumb.listReaders(true);
        csumb.listShelves(true);
        Reader drew = csumb.getReaderByCard(1);
        csumb.checkOutBook(drew,bestServedCold);
        csumb.listReaders(true);
        csumb.returnBook(drew,csumb.getBookByISBN("42-w-87"));
        csumb.returnBook(drew,csumb.getBookByISBN("42-w-87"));

    }
}