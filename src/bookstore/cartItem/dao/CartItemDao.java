package bookstore.cartItem.dao;

import bookstore.book.domain.Book;
import bookstore.cartItem.domain.CartItem;
import bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import tools.commons.CommonUtils;
import tools.jdbc.TxQueryRunner;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: dao layer of CartItem
 *
 * @author csn
 */
public class CartItemDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * Description: add cartItem
     * @param cartItem CartItem Object to be added
     * @throws SQLException
     */
    public void add(CartItem cartItem) throws SQLException {
        String sql = "insert into cartitem (cartItemId,quantity,bid,uid) values(?,?,?,?)";
        Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid()};
        qr.update(sql,params);
    }

    /**
     * Description: find List<CartItem> of a user
     * @param uid unique identifier of a user
     * @return List<CartItem>
     * @throws SQLException
     */
    public List<CartItem> findByUid(String uid) throws SQLException {
        String sql = "select * from cartitem c, book b where c.bid=b.bid and c.uid=?";
        Object[] param = {uid};
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),param);

        return toCartItemList(mapList);
    }

    /**
     * Description: delete cartItem whose id in cartItemsIdArray
     * @param cartItemsIdArray ids to be deleted
     * @throws SQLException
     */
    public void batchDelete(String[] cartItemsIdArray) throws SQLException {
        int len = cartItemsIdArray.length;
        StringBuilder sql = new StringBuilder( "delete from cartitem where cartItemId in (" );

        for(int i = 0; i < len; i++) {
            if(i == len-1) {
                sql.append("?)");
            }
            else sql.append("?,");

        }

        qr.update(sql.toString(),(Object[]) cartItemsIdArray);
    }

    /**
     * Description: update quantity of given cartItem
     * @param cartItemId id of cartItem to be updated
     * @param quantity new quantity value
     * @return CartItem
     * @throws SQLException
     */
    public CartItem updateQuantity(String cartItemId, int quantity) throws SQLException {
        String sql = "update cartitem set quantity=? where cartItemId=?";
        Object[] params = {quantity,cartItemId};
        qr.update(sql,params);
        return findByCartItemId(cartItemId);
    }

    /**
     * Description: find cartItem by cartItemId
     * @param cartItemId id of cartItem
     * @return CartItem
     * @throws SQLException
     */
    private CartItem findByCartItemId(String cartItemId) throws SQLException {
        String sql = "select * from cartitem c, book b where c.bid=b.bid and c.cartItemId=?";
        Map<String,Object> map = qr.query(sql,new MapHandler(),cartItemId);

        return toCartItem(map);
    }

    /**
     * Description: find CartItems by cartItemIds
     * @param cartItemsIdArray ids
     * @return List<CartItem>
     * @throws SQLException
     */
    public List<CartItem> loadCartItems(String[] cartItemsIdArray) throws SQLException {
        int len = cartItemsIdArray.length;
        StringBuilder sql = new StringBuilder( "select * from cartitem c, book b where c.bid = b.bid and cartItemId in (" );

        for(int i = 0; i < len; i++) {
            if(i == len-1) {
                sql.append("?)");
            }
            else sql.append("?,");

        }

        List<Map<String,Object>> mapList =  qr.query(sql.toString(),new MapListHandler(),(Object[])cartItemsIdArray);

        return toCartItemList(mapList);
    }

    /**
     * Description: encapsulate map into CartItem
     * @param map query result
     * @return CartItem
     */
    private CartItem toCartItem(Map map) {

        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map,Book.class);
        User user = CommonUtils.toBean(map,User.class);//uid only
        cartItem.setUser(user);
        cartItem.setBook(book);

        return cartItem;
    }

    /**
     * Description: encapsulate maps into CartItems
     * @param mapList query results
     * @return List<CartItem>
     */
    private List<CartItem> toCartItemList(List<Map<String,Object>> mapList) {
        List<CartItem> cartItems = new ArrayList<>();

        for(Map map : mapList) {
            cartItems.add(toCartItem(map));
        }
        return cartItems;
    }
}
