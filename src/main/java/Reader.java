import Utilities.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    public static final int NAME_=0;
    public static final int PHONE_=1;
    public static final int BOOK_COUNT_=2;
    public static final int CARD_NUMBER_=3;
    public static final int BOOK_START_=4;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books = new ArrayList<>();

    public Reader(int cardNumber, String name, String phone){
        setCardNumber(cardNumber);
        setName(name);
        setPhone(phone);

    }

    public Code addBook(Book book) {
        if (!books.isEmpty()) {
            for (Book i : books) {
                if (i.equals(book)) {
                    return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
                }
            }
        } else {
            books.add(book);
            return Code.SUCCESS;
        }
        return Code.BOOK_COUNT_ERROR;
    }

    public Code removeBook(Book book){
        for(Book i : books){
            if(i.equals(book)){
                books.remove(i);
                return Code.SUCCESS;
            }
        }
        return Code.READER_DOESNT_HAVE_BOOK_ERROR;
    }

    public boolean hasBook(Book book){
        for(Book i : books){
            if(i.equals(book)){
                return true;
            }
        }
        return false;
    }

    public int getBookCount(){
        return books.size();
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        if(books!=(null)){
            this.books = books;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return cardNumber == reader.cardNumber && Objects.equals(name, reader.name) && Objects.equals(phone, reader.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, name, phone);
    }

    @Override
    public String toString() {
        return String.format("%s (#%s) has checked out {%s}", name, cardNumber, books);
    }
}
