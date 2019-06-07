package bookstore.order.servlet;

import bookstore.cartItem.domain.CartItem;
import bookstore.cartItem.service.CartItemService;
import bookstore.order.domain.Order;
import bookstore.order.domain.OrderItem;
import bookstore.order.service.OrderService;
import bookstore.paging.PageBean;
import bookstore.user.domain.User;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: web layer of Order
 *
 * @author csn
 */
public class OrderServlet extends BaseServlet {
    private OrderService orderService = new OrderService();
    private CartItemService cartItemService = new CartItemService();

    /**
     * Description: generate order and delete related cartitems
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String generateOrder(HttpServletRequest req, HttpServletResponse resp) {

        //从req获取参数
        String address = req.getParameter("address");
        double total = Double.parseDouble(req.getParameter("total"));

        String[] cartItemIdsArray = req.getParameter("cartItemIds").split(",");
        //根据ids获取cartItemList
        List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIdsArray);

        //校验空订单
        if(cartItemList.size() == 0) {
            req.setAttribute("code","error");
            req.setAttribute("msg","您尚未选择任何商品，不能下单");
            return "f:/jsps/msg.jsp";
        }

        //获取user
        User owner = (User)req.getSession().getAttribute("sessionUser");

        //设置
        Order order = new Order();
        order.setOid(CommonUtils.uuid());//设置主键
        order.setOrdertime(String.format("%tF %<tT", new Date()));//下单时间
        order.setStatus(1);//设置状态，1表示未付款
        order.setAddress(address);//地址
        order.setTotal(total);//总金额
        order.setOwner(owner);//所属用户

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItemList) {
            OrderItem oi = new OrderItem();
            oi.setOrderItemId(CommonUtils.uuid());
            oi.setQuantity(cartItem.getQuantity());
            oi.setSubtotal(cartItem.getSubTotal());
            oi.setBook(cartItem.getBook());
            oi.setOrder(order);

            orderItems.add(oi);
        }

        order.setOrderItems(orderItems);

        //完成添加
        orderService.generateOrder(order);

        //从购物车条目中删除
        cartItemService.batchDelete(cartItemIdsArray);

        req.setAttribute("order",order);

        return "f:/jsps/order/ordersucc.jsp";
    }

    /**
     * Description: 我的订单
     * @param req
     * @param resp
     * @return
     */
    public String myOrders(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);
        String url = getUrl(req);

        User owner = (User)req.getSession().getAttribute("sessionUser");

        PageBean<Order> ordersPageBean = orderService.findByUid(currPage,owner.getUid());

        ordersPageBean.setUrl(url);
        req.setAttribute("pageBean",ordersPageBean);

        return "f:/jsps/order/list.jsp";
    }

    /**
     * Description: 去除url的currPage参数
     * @param req
     * @return String
     */
    private String getUrl(HttpServletRequest req) {
        String url = req.getRequestURI() + "?" + req.getQueryString();

        int id = url.lastIndexOf("&currPage=");
        if(id != -1) url = url.substring(0,id);
        return url;
    }

    /**
     * Description: get currPage from req
     * @param req
     * @return int
     */
    private int getCurrPage(HttpServletRequest req) {
        String currPage = req.getParameter("currPage");

        //req中没有参数则为1，有则转换为int
        if(currPage == null || currPage.trim().isEmpty()) return 1;
        else return Integer.parseInt(currPage);
    }

    /**
     * Description: query Order by oid
     * @param req
     * @param resp
     * @return String
     */
    public String load(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");

        Order order = orderService.loadOrder(oid);

        req.setAttribute("order",order);
        return "f:/jsps/order/desc.jsp";
    }
}
