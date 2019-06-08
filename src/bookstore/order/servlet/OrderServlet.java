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
     * Description: query Order by oid, set btn，查看订单
     * @param req
     * @param resp
     * @return String
     */
    public String load(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");
        String btn = req.getParameter("btn");

        Order order = orderService.loadOrder(oid);

        req.setAttribute("order",order);
        req.setAttribute("btn",btn);
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * Description: prepare payment
     * @param req
     * @param resp
     * @return String
     */
    public String prePay(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");

        Order order = orderService.loadOrder(oid);
        req.setAttribute("order",order);
        return "f:/jsps/order/pay.jsp";
    }

    /**
     * Description: fake payment，1->3,ignore 准备发货
     * @param req
     * @param resp
     * @return String
     */
    public String pay(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");

        //更新status为等待确认
        orderService.updateStatus(oid,3);
        //支付成功并转发请求
        req.setAttribute("code","success");
        req.setAttribute("msg","订单支付成功！");
        return "f:/jsps/msg.jsp";
    }


    /**
     * Description: cancel order when status eq 1 ,1->5
     * @param req
     * @param resp
     * @return String
     */
    public String cancel(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");
        int status = orderService.findStatusByOid(oid);
        //校验订单状态
        if(status != 1) {
            req.setAttribute("code","error");
            req.setAttribute("msg","不能取消该订单");
            return "f:/jsps/msg.jsp";
        }
        //更新
        orderService.updateStatus(oid,5);
        req.setAttribute("code","success");
        req.setAttribute("msg","订单取消成功");
        return "f:/jsps/msg.jsp";
    }

    /**
     * Description: confirm order when status eq 3, 3->4
     * @param req
     * @param resp
     * @return String
     */
    public String confirm(HttpServletRequest req, HttpServletResponse resp) {
        String oid = req.getParameter("oid");
        int status = orderService.findStatusByOid(oid);
        //校验订单状态
        if(status != 3) {
            req.setAttribute("code","error");
            req.setAttribute("msg","不能确认该订单");
            return "s:/jsps/msg.jsp";
        }
        //更新
        orderService.updateStatus(oid,4);
        req.setAttribute("code","success");
        req.setAttribute("msg","交易成功");
        return "f:/jsps/msg.jsp";
    }

}
