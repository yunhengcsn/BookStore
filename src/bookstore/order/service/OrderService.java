package bookstore.order.service;

import bookstore.order.dao.OrderDao;
import bookstore.order.domain.Order;
import tools.jdbc.JdbcUtils;

import java.sql.SQLException;

/**
 * Description:
 *
 * @author csn
 */
public class OrderService {
    private OrderDao orderDao = new OrderDao();

    /**
     * Description: 在一个事务中添加订单
     * @param order 订单
     */
    public void generateOrder(Order order) {
        try {
            JdbcUtils.beginTransaction();
            orderDao.addOrder(order);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
