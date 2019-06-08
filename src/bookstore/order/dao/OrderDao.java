package bookstore.order.dao;

import bookstore.book.domain.Book;
import bookstore.order.domain.Order;
import bookstore.order.domain.OrderItem;
import bookstore.paging.Expression;
import bookstore.paging.PageBean;
import bookstore.paging.PageConstants;
import bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import tools.commons.CommonUtils;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Description: find orders by uid
     * @param currPage
     * @param uid
     * @return PageBean<Order>
     * @throws SQLException
     */
    public PageBean<Order> findByUid(int currPage, String uid) throws SQLException {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new Expression("uid","=",uid));

        return findOrdersByCriteria(expressionList,currPage);
    }

    /**
     * Description: 查询方法
     * @param expressionList
     * @param currPage
     * @return PageBean<Book>
     */
    private PageBean<Order> findOrdersByCriteria(List<Expression> expressionList, int currPage) throws SQLException {
        //每页记录数
        int pageSize = PageConstants.ORDER_PAGE_SIZE;

        StringBuilder whereSql = new StringBuilder("where ");//where条件
        List<Object> params = new ArrayList<>();//参数列表

        for(int i = 0,size = expressionList.size(); i < size; i++) {
            //第一个参数不加and
            if(i != 0) {
                whereSql.append(" and ");
            }
            //where条件拼接 column = ?等
            whereSql.append(expressionList.get(i).getName()).append(' ').append(expressionList.get(i).getOperator());

            //若运算符不为is null，则加参数和？;否则pass
            if(! expressionList.get(i).getOperator().equals("is null")) {
                whereSql.append(" ?");
                params.add(expressionList.get(i).getValue());
            }
        }

        //查询总记录数，赋值给pageBean属性
        Number totalRecords;
        String sql = "select count(*) from orders " + whereSql;
        try {
            totalRecords = (Number) qr.query(sql,new ScalarHandler(),params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询总记录数失败");
        }


        //按条件查询orderList
        List<Order> orderList;
        sql = "select * from orders " + whereSql + " order by ordertime desc limit ?,?";
        params.add((currPage-1) * pageSize);
        params.add(pageSize);
        try {
            orderList = qr.query(sql,new BeanListHandler<>(Order.class), params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败");
        }

        //给orderList中每个order的orderItems属性赋值
        for(Order order : orderList) {
            order.setOrderItems(loadOrderItem(order));
        }

        //设置pageBean,url由servlet设置
        PageBean<Order> pageBean = new PageBean<>();

        pageBean.setBeanList(orderList);
        pageBean.setCurrPage(currPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalRecords(totalRecords.intValue());

        return pageBean;
    }

    /**
     * Description: find orderItems by order
     * @param order
     * @return List<OrderItem>
     * @throws SQLException
     */
    private List<OrderItem> loadOrderItem(Order order) throws SQLException {
        String sql = "select * from orderitem od, book b where od.bid=b.bid and od.oid=?";
        Object[] param = {order.getOid()};

        List<Map<String,Object>> mapList =  qr.query(sql,new MapListHandler(),param);
        List<OrderItem> orderItemList = new ArrayList<>();
        for(Map map : mapList) {
            Book book = CommonUtils.toBean(map,Book.class);
            OrderItem orderItem = CommonUtils.toBean(map,OrderItem.class);

            orderItem.setBook(book);
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * Description: find order by oid
     * @param oid
     * @return Order
     * @throws SQLException
     */
    public Order findByOid(String oid) throws SQLException {
        String sql = "select * from orders where oid=?";
        Order order = qr.query(sql,new BeanHandler<>(Order.class),oid);
        //set OrderItems
        order.setOrderItems(loadOrderItem(order));
        return order;
    }

    /**
     * Description: find order by oid
     * @param oid
     * @return int
     */
    public int findStatusByOid(String oid) throws SQLException {
        String sql = "select status from orders where oid=?";

        return (int)qr.query(sql,new ScalarHandler(),oid);
    }

    /**
     * Description: set new status of oid
     * @param oid
     * @param status
     */
    public void updateStatus(String oid, int status) throws SQLException {
        String sql = "update orders set status=? where oid=?";
        Object[] params = {status,oid};

        qr.update(sql,params);
    }
}
