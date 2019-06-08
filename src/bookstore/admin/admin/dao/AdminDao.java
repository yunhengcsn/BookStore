package bookstore.admin.admin.dao;

import bookstore.admin.admin.domain.Admin;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;

/**
 * Description:
 *
 * @author csn
 */
public class AdminDao {
    private QueryRunner qr = new TxQueryRunner();

    /**
     * 查询管理员
     * @param adminname
     * @param adminpwd
     * @return
     */
    public Admin findAdmin(String adminname, String adminpwd) throws SQLException {
        String sql = "select * from admin where adminname=? and adminpwd=?";
        Object[] params = {adminname,adminpwd};

        return qr.query(sql,new BeanHandler<>(Admin.class),params);
    }
}
