package com.example.ekmuthoboi;

public class CartList {
    private String bookId,bookName,bookTotalPrice,bookDollerPrice,quantity,userId;

    public CartList(String bookId, String bookName, String bookTotalPrice,String bookDollerPrice, String quantity,String userId) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookTotalPrice = bookTotalPrice;
        this.bookDollerPrice = bookDollerPrice;
        this.quantity = quantity;
        this.userId = userId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookTotalPrice() {
        return bookTotalPrice;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookDollerPrice() {
        return bookDollerPrice;
    }

    public void setBookDollerPrice(String bookDollerPrice) {
        this.bookDollerPrice = bookDollerPrice;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBookTotalPrice(String bookTotalPrice) {
        this.bookTotalPrice = bookTotalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
