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
     * Description: 分页 根据uid查找Order列表，多个操作 放入事务
     * @param currPage
     * @param uid
     * @return PageBean<Order>
     */
    public PageBean<Order> findByUid(int currPage, String uid) {
        PageBean<Order> orderPageBean = new PageBean<>();
        try {
            JdbcUtils.beginTransaction();
            orderPageBean = orderDao.findByUid(currPage,uid);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return  orderPageBean;
    }

    /**
     * Description: query Order by oid，多个操作 放入事务
     * @param oid
     * @return Order
     */
    public Order loadOrder(String oid) {
        Order order = new Order();
        try {
            JdbcUtils.beginTransaction();
            order = orderDao.findByOid(oid);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return order;
    }

    /**
     * Description: find status by oid
     * @param oid
     * @return int
     */
    public int findStatusByOid(String oid) {
        int status = -1;
        try {
            status = orderDao.findStatusByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Description: update status
     * @param oid
     * @param status
     */
    public void updateStatus(String oid, int status) {
        try {
            orderDao.updateStatus(oid,status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
