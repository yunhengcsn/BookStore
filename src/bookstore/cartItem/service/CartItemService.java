package bookstore.cartItem.service;

import bookstore.cartItem.dao.CartItemDao;
import bookstore.cartItem.domain.CartItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 *
 * @author csn
 */
public class CartItemService {
    private CartItemDao cartItemDao = new CartItemDao();

    /**
     * Description:添加购物车条目
     * @param cartItem
     */
    public void add(CartItem cartItem) {
        try {
            cartItemDao.add(cartItem);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("添加购物车条目失败！");
        }
    }

    /**
     * Description:  get cartItemList of a user by uid
     * @param uid
     * @return List<CartItem>
     */
    public List<CartItem> myCart(String uid) {
        try {
            return cartItemDao.findByUid(uid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询我的购物车失败！");
        }
    }
}
