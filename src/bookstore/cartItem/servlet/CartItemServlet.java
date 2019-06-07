package bookstore.cartItem.servlet;

import bookstore.book.domain.Book;
import bookstore.cartItem.domain.CartItem;
import bookstore.cartItem.service.CartItemService;
import bookstore.user.domain.User;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Description: web layer of CartItem
 *
 * @author csn
 */
public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService = new CartItemService();

    /**
     * Description: 向购物车添加条目
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String add(HttpServletRequest req, HttpServletResponse resp) {

        //封装cartItem对象和Book对象
        Map map = req.getParameterMap();
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map,Book.class);
        //设置属性
        cartItem.setBook(book);
        cartItem.setCartItemId(CommonUtils.uuid());
        cartItem.setUser((User)req.getSession().getAttribute("sessionUser"));

        cartItemService.add(cartItem);

        return myCart(req,resp);
    }

    /**
     * Description: 我的购物车，获取用户所有购物车条目
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String myCart(HttpServletRequest req, HttpServletResponse resp) {
        User sessionUser = (User) req.getSession().getAttribute("sessionUser");
        String uid = sessionUser.getUid();

        List<CartItem> cartItemList = cartItemService.myCart(uid);

        req.setAttribute("cartItemList",cartItemList);

        return "f:/jsps/cart/list.jsp";
    }

    /**
     * Description: delete cartItems whose id in cartItemIds
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String batchDelete(HttpServletRequest req, HttpServletResponse resp) {
        String cartItemIds = req.getParameter("cartItemIds");
        String[] cartItemsIdArray = cartItemIds.split(",");

        cartItemService.batchDelete(cartItemsIdArray);

        return myCart(req,resp);
    }

    /**
     * Description: update quantity of a cartItemId
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String updateQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String cartItemId = req.getParameter("cartItemId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        CartItem cartItem = cartItemService.updateQuantity(cartItemId,quantity);

        // 给客户端返回一个json对象

        String sb = "{" + "\"quantity\"" + ":" + cartItem.getQuantity() +
                "," +
                "\"subTotal\"" + ":" + cartItem.getSubTotal() +
                "}";
        resp.getWriter().print(sb);

        return null;
    }

    /**
     * Description: 加载cartItems详情
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return String
     */
    public String loadCartItems(HttpServletRequest req, HttpServletResponse resp) {
        //获取要生成订单的cartItemIds和总金额total
        String cartItemIds = req.getParameter("cartItemIds");
        String total = req.getParameter("total");

        String[] cartItemsIdArray = cartItemIds.split(",");

        List<CartItem> cartItems = cartItemService.loadCartItems(cartItemsIdArray);

        req.setAttribute("cartItems",cartItems);
        req.setAttribute("total",total);
        req.setAttribute("cartItemIds",cartItemIds);

        return "f:/jsps/cart/showitem.jsp";

    }

}
