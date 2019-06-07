package bookstore.order.dao;

import bookstore.order.domain.Order;
import bookstore.order.domain.OrderItem;
import org.apache.commons.dbutils.QueryRunner;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;

/**
 * Description: dao layer of Order
 *
 * @author csn
 */
public class OrderDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * Description:向数据库写入order和orderitem
     * @param order 订单
     */
    public void addOrder(Order order) throws SQLException {
        //向order表添加数据
        String sql = "insert into orders (oid,ordertime,total,status,address,uid) values(?,?,?,?,?,?)";
        Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),order.getStatus(),order.getAddress(),order.getOwner().getUid()};

        qr.update(sql,params);

        //遍历orderItems并添加入orderitem表
        String sql2 = "insert into orderitem (orderItemId,quantity,subtotal,bid,bname,currPrice,image_b,oid) values(?,?,?,?,?,?,?,?)";

        Object[][] batchParams = new Object[order.getOrderItems().size()][8];
        int i = 0, j = 0;
        for(OrderItem oi : order.getOrderItems()) {
            batchParams[i][j++] = oi.getOrderItemId();
            batchParams[i][j++] = oi.getQuantity();
            batchParams[i][j++] = oi.getSubtotal();
            batchParams[i][j++] = oi.getBook().getBid();
            batchParams[i][j++] = oi.getBook().getBname();
            batchParams[i][j++] = oi.getBook().getCurrPrice();
            batchParams[i][j++] = oi.getBook().getImage_b();
            batchParams[i][j] = oi.getOrder().getOid();
            i++;
            j = 0;
        }
        //批处理
        qr.batch(sql2,batchParams);
    }
}
