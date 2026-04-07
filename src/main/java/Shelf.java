import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public int SHELF_NUMBER_;
    public int SUBJECT_;
    private HashMap<Book, Integer> books;
    private int shelfNumber;
    private String subject;

    public Shelf(int shelfNumber, String subject){
        setShelfNumber(shelfNumber);
        setSubject(subject);
    }

    public Code addBook(Book book){
        return;
    }

    public int getBookCount(Book book){
        return 0;
    }

    public String listBooks(){
        return "0";
    }

    public Code removeBooks(Book book){
        return;
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