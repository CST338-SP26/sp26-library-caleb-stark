import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public static final int SHELF_NUMBER_=0;
    public static final int SUBJECT_=1;
    private HashMap<Book, Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf() {
        books = new HashMap<>();
    }

    public Shelf(int shelfNumber, String subject){
        books = new HashMap<>();
        setShelfNumber(shelfNumber);
        setSubject(subject);
    }

    public Code addBook(Book book){
        if(books.containsKey(book)){
            books.put(book, books.get(book) + 1);
            System.out.println(book + " added to shelf " + this);
            return Code.SUCCESS;
        }
        if(!Objects.equals(book.getSubject(), subject)){
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
        books.put(book, 1);
        System.out.println(book.getTitle() + " added to shelf " + this);
        return Code.SUCCESS;
    }

    public int getBookCount(Book book){
        if(!books.containsKey(book)){
            return -1;
        }
        return books.get(book);
    }

    public String listBooks(){
        int totalBooks = 0;
        for(int count : books.values()){
            totalBooks += count;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(totalBooks).append(" books on shelf: ").append(this);
        for(Book book : books.keySet()){
            sb.append("/n").append(book).append(" ").append(books.get(book));
        }
        return sb.toString();
    }

    public Code removeBook(Book book){
        if(!books.containsKey(book)){
            System.out.println(book.getTitle() + " is not on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(books.get(book) == 0){
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        books.put(book, books.get(book) - 1);
        System.out.println(book.getTitle() + " successfully removed from shelf " + subject);
        return Code.SUCCESS;
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }
}