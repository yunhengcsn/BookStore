package bookstore.cartItem.service;

import bookstore.cartItem.dao.CartItemDao;
import bookstore.cartItem.domain.CartItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: service layer of CartItem
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

    /**
     * Description: delete cartItem whose id in cartItemsIdArray
     * @param cartItemsIdArray ids to be deleted
     */
    public void batchDelete(String[] cartItemsIdArray) {
        try {
            cartItemDao.batchDelete(cartItemsIdArray);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除购物车条目失败！");
        }
    }

    /**
     * Description: update quantity of given cartItem
     * @param cartItemId id of cartItem to be updated
     * @param quantity new quantity value
     * @return CartItem
     */
    public CartItem updateQuantity(String cartItemId, int quantity) {
        try {
            return cartItemDao.updateQuantity(cartItemId,quantity);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("更新购物车条目失败！");
        }
    }

    /**
     * Description: get cartItems whose id in cartItemsIdArray
     * @param cartItemsIdArray ids to be queried
     * @return List<CartItem>
     */
    public List<CartItem> loadCartItems(String[] cartItemsIdArray) {
        try {
            return cartItemDao.loadCartItems(cartItemsIdArray);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("拉取购物车条目失败！");
        }
    }
}
