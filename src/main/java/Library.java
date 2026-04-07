import Utilities.Code;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/*
 * Abstract:
 * Name: Caleb Stark
 * Date: 04/06/2026
 */

public class Library {
    public static final int LENDING_LIMIT = 5;
    private static int libraryCard;
    private HashMap<Book, Integer> books;
    private String name;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;

    public Library(String s){
        name = s;
        books = new HashMap<>();
        readers = new ArrayList<>();
        shelves = new HashMap<>();
    }

    public Code addBook(Book book){
        if(books.containsKey(book)){
            books.put(book, books.get(book) + 1);
        }else{
            books.put(book, 1);
        }

        if(shelves.containsKey(book.getSubject())){
            shelves.get(book.getSubject()).addBook(book);
            return Code.SUCCESS;
        }

        return Code.SHELF_EXISTS_ERROR;
    }

    private Code addBookToShelf(Book book, Shelf shelf){
        return shelf.addBook(book);
    }

    public Code addReader(Reader reader){
        for(Reader r : readers){
            if(r.equals(reader)){
                return Code.READER_ALREADY_EXISTS_ERROR;
            }
            if(r.getCardNumber() == reader.getCardNumber()){
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }
        readers.add(reader);
        if(reader.getCardNumber() > libraryCard){
            libraryCard = reader.getCardNumber();
        }
        return Code.SUCCESS;
    }

    public Code addShelf(Shelf shelf){
        if(shelves.containsKey(shelf.getSubject())){
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.put(shelf.getSubject(), shelf);
        return Code.SUCCESS;
    }

    public Code addShelf(String s){
        Shelf shelf = new Shelf(shelves.size() + 1, s);
        return addShelf(shelf);
    }

    public Code checkOutBook(Reader reader, Book book){
        if(!readers.contains(reader)){
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBookCount() >= LENDING_LIMIT){
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!books.containsKey(book)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(!shelves.containsKey(book.getSubject())){
            return Code.SHELF_EXISTS_ERROR;
        }
        if(shelves.get(book.getSubject()).getBookCount(book) < 1){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        Code code = reader.addBook(book);
        if(code != Code.SUCCESS){
            return code;
        }
        return shelves.get(book.getSubject()).removeBook(book);
    }

    public static LocalDate convertDate(String s, Code c){
        if(s.equals("0000")){
            return LocalDate.of(1970, 1, 1);
        }
        String[] parts = s.split("-");
        if(parts.length != 3){
            return LocalDate.of(1970, 1, 1);
        }
        int year = convertInt(parts[0], c);
        int month = convertInt(parts[1], c);
        int day = convertInt(parts[2], c);
        if(year < 0 || month < 0 || day < 0){
            return LocalDate.of(1970, 1, 1);
        }
        try{
            return LocalDate.of(year, month, day);
        }catch(Exception e){
            return LocalDate.of(1970, 1, 1);
        }
    }

    public static int convertInt(String s, Code c){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException e){
            return c.getCode();
        }
    }

    private Code errorCode(int i){
        for(Code code : Code.values()){
            if(code.getCode() == i){
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    public Book getBookByISBN(String s){
        for(Book book : books.keySet()){
            if(book.getISBN().equals(s)){
                return book;
            }
        }
        return null;
    }

    public static int getLibraryCardNumber(){
        return libraryCard + 1;
    }

    public String getName(){
        return name;
    }

    public Reader getReaderByCard(int i){
        for(Reader reader : readers){
            if(reader.getCardNumber() == i){
                return reader;
            }
        }
        return null;
    }

    public Shelf getShelf(String s){
        return shelves.get(s);
    }

    public Shelf getShelf(Integer i){
        for(Shelf shelf : shelves.values()){
            if(shelf.getShelfNumber() == i){
                return shelf;
            }
        }
        return null;
    }

    public Code init(String s){
        try{
            Scanner sc = new Scanner(new java.io.File(s));
            if(!sc.hasNextLine()){
                sc.close();
                return Code.LIBRARY_ERROR;
            }
            int bookCount = convertInt(sc.nextLine(), Code.BOOK_COUNT_ERROR);
            if(bookCount < 0){
                sc.close();
                return errorCode(bookCount);
            }
            Code code = initBooks(bookCount, sc);
            if(code != Code.SUCCESS){
                sc.close();
                return code;
            }
            if(!sc.hasNextLine()){
                sc.close();
                return Code.SHELF_COUNT_ERROR;
            }
            int shelfCount = convertInt(sc.nextLine(), Code.SHELF_COUNT_ERROR);
            if(shelfCount < 0){
                sc.close();
                return errorCode(shelfCount);
            }
            code = initShelves(shelfCount, sc);
            if(code != Code.SUCCESS){
                sc.close();
                return code;
            }
            if(!sc.hasNextLine()){
                sc.close();
                return Code.READER_COUNT_ERROR;
            }
            int readerCount = convertInt(sc.nextLine(), Code.READER_COUNT_ERROR);
            if(readerCount < 0){
                sc.close();
                return errorCode(readerCount);
            }
            code = initReader(readerCount, sc);
            sc.close();
            return code;
        }catch(java.io.FileNotFoundException e){
            return Code.FILE_NOT_FOUND_ERROR;
        }
    }

    private Code initBooks(int i, Scanner sc){
        if(i < 1){
            return Code.LIBRARY_ERROR;
        }
        for(int j = 0; j < i; j++){
            if(!sc.hasNextLine()){
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            String[] lineBook = sc.nextLine().split(",");
            if(lineBook.length <= Book.DUE_DATE_){
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            int pageCount = convertInt(lineBook[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            if(pageCount <= 0){
                return Code.PAGE_COUNT_ERROR;
            }
            LocalDate dueDate = convertDate(lineBook[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            Book book = new Book(lineBook[Book.ISBN_], lineBook[Book.TITLE_], lineBook[Book.SUBJECT_], pageCount, lineBook[Book.AUTHOR_], dueDate);
            if(books.containsKey(book)){
                books.put(book, books.get(book) + 1);
            }else{
                books.put(book, 1);
            }
        }
        return Code.SUCCESS;
    }

    private Code initReader(int i, Scanner sc){
        if(i <= 0){
            return Code.READER_COUNT_ERROR;
        }
        for(int j = 0; j < i; j++){
            if(!sc.hasNextLine()){
                return Code.READER_COUNT_ERROR;
            }
            String[] lineReader = sc.nextLine().split(",");
            int cardNumber = convertInt(lineReader[Reader.CARD_NUMBER_], Code.READER_CARD_NUMBER_ERROR);
            Reader reader = new Reader(cardNumber, lineReader[Reader.NAME_], lineReader[Reader.PHONE_]);
            addReader(reader);
            int bookCount = convertInt(lineReader[Reader.BOOK_COUNT_], Code.BOOK_COUNT_ERROR);
            int index = Reader.BOOK_START_;
            for(int k = 0; k < bookCount; k++){
                if(index + 1 >= lineReader.length){
                    break;
                }
                Book book = getBookByISBN(lineReader[index]);
                if(book != null){
                    reader.addBook(book);
                }
                index += 2;
            }
        }
        return Code.SUCCESS;
    }

    private Code initShelves(int i, Scanner sc){
        if(i < 1){
            return Code.SHELF_COUNT_ERROR;
        }
        for(int j = 0; j < i; j++){
            if(!sc.hasNextLine()){
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            String[] lineShelf = sc.nextLine().split(",");
            if(lineShelf.length <= Shelf.SUBJECT_){
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            int shelfNumber = convertInt(lineShelf[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR);
            if(shelfNumber < 0){
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            Shelf shelf = new Shelf(shelfNumber, lineShelf[Shelf.SUBJECT_]);
            addShelf(shelf);
        }
        for(Book book : books.keySet()){
            if(shelves.containsKey(book.getSubject())){
                int count = books.get(book);
                for(int k = 0; k < count; k++){
                    shelves.get(book.getSubject()).addBook(book);
                }
            }
        }
        return Code.SUCCESS;
    }

    public int listBooks(){
        int total = 0;
        for(int count : books.values()){
            total += count;
        }
        return total;
    }

    public int listReaders(){
        return readers.size();
    }

    public int listReaders(boolean b){
        return readers.size();
    }

    public int listShelves(boolean b){
        return shelves.size();
    }

    public int listShelves(){
        return shelves.size();
    }

    public Code removeReader(Reader reader){
        if(!readers.contains(reader)){
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBookCount() > 0){
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        }
        readers.remove(reader);
        return Code.SUCCESS;
    }

    public Code returnBook(Reader reader, Book book){
        if(!reader.hasBook(book)){
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        if(!books.containsKey(book)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        Code code = reader.removeBook(book);
        if(code != Code.SUCCESS){
            return code;
        }
        return returnBook(book);
    }

    public Code returnBook(Book book){
        if(!shelves.containsKey(book.getSubject())){
            return Code.SHELF_EXISTS_ERROR;
        }
        return shelves.get(book.getSubject()).addBook(book);
    }
}