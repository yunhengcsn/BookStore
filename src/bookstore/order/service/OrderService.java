package bookstore.order.service;

import bookstore.order.dao.OrderDao;
import bookstore.order.domain.Order;
import bookstore.paging.PageBean;
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

    /**
     * Description: 分页 根据uid查找Order列表
     * @param currPage
     * @param uid
     * @return PageBean<Order>
     */
    public PageBean<Order> findByUid(int currPage, String uid) {
        PageBean<Order> orderPageBean = new PageBean<>();
        try {
            orderPageBean = orderDao.findByUid(currPage,uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  orderPageBean;
    }

    /**
     * Description: query Order by oid
     * @param oid
     * @return Order
     */
    public Order loadOrder(String oid) {
        Order order = new Order();
        try {
            order = orderDao.findByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

}
