package bookstore.cartItem.dao;

import bookstore.book.domain.Book;
import bookstore.cartItem.domain.CartItem;
import bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import tools.commons.CommonUtils;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author csn
 */
public class CartItemDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * Description: add cartItem
     * @param cartItem
     * @throws SQLException
     */
    public void add(CartItem cartItem) throws SQLException {
        String sql = "insert into cartitem (cartItemId,quantity,bid,uid) values(?,?,?,?)";
        Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid()};
        qr.update(sql,params);
    }

    /**
     * Description: find List<CartItem> of a user
     * @param uid
     * @return List<CartItem>
     * @throws SQLException
     */
    public List<CartItem> findByUid(String uid) throws SQLException {
        String sql = "select * from cartitem c, book b where c.bid=b.bid and c.uid=?";
        Object[] param = {uid};
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),param);

        List<CartItem> cartItemList = new ArrayList<>();
        for(Map map : mapList) {
            CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
            Book book = CommonUtils.toBean(map,Book.class);
            User user = CommonUtils.toBean(map,User.class);//uid only
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItemList.add(cartItem);
        }
        return cartItemList;
    }
}
