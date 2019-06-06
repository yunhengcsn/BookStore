package bookstore.cartItem.servlet;

import bookstore.book.domain.Book;
import bookstore.cartItem.domain.CartItem;
import bookstore.cartItem.service.CartItemService;
import bookstore.user.domain.User;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Description:
 *
 * @author csn
 */
public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService = new CartItemService();

    /**
     * Description: 向购物车添加条目
     * @param req
     * @param resp
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
     * @param req
     * @param resp
     * @return
     */
    public String myCart(HttpServletRequest req, HttpServletResponse resp) {
        User sessionUser = (User) req.getSession().getAttribute("sessionUser");
        String uid = sessionUser.getUid();

        List<CartItem> cartItemList = cartItemService.myCart(uid);

        req.setAttribute("cartItemList",cartItemList);

        return "f:/jsps/cart/list.jsp";
    }
}
