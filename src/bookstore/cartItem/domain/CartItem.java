package bookstore.cartItem.domain;

import bookstore.book.domain.Book;
import bookstore.user.domain.User;

import java.math.BigDecimal;

/**
 * Description: model of CartItem
 *
 * @author csn
 */
/*
 *+------------+----------+------+-----+---------+----------------+
| Field      | Type     | Null | Key | Default | Extra          |
+------------+----------+------+-----+---------+----------------+
| cartItemId | char(32) | NO   | PRI | NULL    |                |
| quantity   | int(11)  | YES  |     | NULL    |                |
| bid        | char(32) | YES  | MUL | NULL    |                |
| uid        | char(32) | YES  | MUL | NULL    |                |
| orderBy    | int(11)  | NO   | MUL | NULL    | auto_increment |
+------------+----------+------+-----+---------+----------------+
 */
public class CartItem {
    private String cartItemId;
    private int quantity;
    private Book book;
    private User user;

    //使用BigDecimal去误差
    public double getSubTotal() {
        BigDecimal q = new BigDecimal(quantity+"");//构造函数入参用String
        BigDecimal cp = new BigDecimal(book.getCurrPrice()+"");

        return q.multiply(cp).doubleValue();
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId='" + cartItemId + '\'' +
                ", quantity=" + quantity +
                ", book=" + book +
                ", user=" + user +
                '}';
    }
}
