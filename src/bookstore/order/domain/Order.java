package bookstore.order.domain;

import bookstore.user.domain.User;
import java.util.List;

/**
 * Description: Order of a user, related to OrderItem and User
 *+-----------+---------------+------+-----+---------+-------+
 * | Field     | Type          | Null | Key | Default | Extra |
 * +-----------+---------------+------+-----+---------+-------+
 * | oid       | char(32)      | NO   | PRI | NULL    |       |
 * | ordertime | char(19)      | YES  |     | NULL    |       |
 * | total     | decimal(10,2) | YES  |     | NULL    |       |
 * | status    | int(11)       | YES  |     | NULL    |       |
 * | address   | varchar(1000) | YES  |     | NULL    |       |
 * | uid       | char(32)      | YES  | MUL | NULL    |       |
 * +-----------+---------------+------+-----+---------+-------+
 * @author csn
 */
public class Order {
    private String oid;//订单id
    private String ordertime;//订单生成时间
    private double total;//总金额
    private int status;//订单状态
    private String address;//收货地址

    private User owner;//所属用户

    private List<OrderItem> orderItems;//订单条目列表

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", ordertime='" + ordertime + '\'' +
                ", total=" + total +
                ", status=" + status +
                ", address='" + address + '\'' +
                ", owner=" + owner +
                ", orderItems=" + orderItems +
                '}';
    }
}
