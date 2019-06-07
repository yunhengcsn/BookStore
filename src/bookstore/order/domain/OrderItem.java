package bookstore.order.domain;

import bookstore.book.domain.Book;

/**
 * Description: Order of an order, related to Book
 *+-------------+--------------+------+-----+---------+-------+
 * | Field       | Type         | Null | Key | Default | Extra |
 * +-------------+--------------+------+-----+---------+-------+
 * | orderItemId | char(32)     | NO   | PRI | NULL    |       |
 * | quantity    | int(11)      | YES  |     | NULL    |       |
 * | subtotal    | decimal(8,2) | YES  |     | NULL    |       |
 * | bid         | char(32)     | YES  |     | NULL    |       |
 * | bname       | varchar(200) | YES  |     | NULL    |       |
 * | currPrice   | decimal(8,2) | YES  |     | NULL    |       |
 * | image_b     | varchar(100) | YES  |     | NULL    |       |
 * | oid         | char(32)     | YES  | MUL | NULL    |       |
 * +-------------+--------------+------+-----+---------+-------+
 * @author csn
 */
public class OrderItem {
    private String orderItemId;//订单条目id
    private int quantity;//图书数量
    private double subtotal;//小计金额

    private Book book;//所含图书
    private Order order;//所属订单

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", book=" + book +
                ", order=" + order +
                '}';
    }
}
