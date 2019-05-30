package bookstore.user.dao;

import bookstore.user.domain.User;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.dao
 * @Author: csn
 * @CreateTime: 2019-05-23 15:21
 * @Description: dao layer of user module
 */
public class UserDao {
    private TxQueryRunner qr = new TxQueryRunner();

    /*
     * @Description: add form into database
     * @Param: [form]
     * @return void
     **/
    public void add(User form) {
        String sql = "insert into user values(?,?,?,?,?,?)";
        Object[] params = {form.getUid(),form.getUsername(),form.getPassword(),form.getEmail(),form.getStatus(),form.getActivationCode()};

        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("增加用户失败");
        }
    }

    /*
     * @Description: check if username exists in database
     * @Param: [username]
     * @return boolean
     **/
    public boolean findByName(String username) {
        String sql = "select count(*) from user where username=?";
        Object[] param = {username};
        Number res = 0;

        try {
            res = (Number) qr.query(sql,new ScalarHandler(),param);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("按用户名查询失败");
        }
        return res.intValue() == 0;
    }

    /*
     * @Description: check if email exists in database
     * @Param: [email]
     * @return boolean
     **/
    public boolean findByEmail(String email) {
        String sql = "select count(*) from user where email=?";
        Object[] param = {email};
        Number res = 0;

        try {
            res = (Number) qr.query(sql,new ScalarHandler(),param);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("按邮箱查询失败");
        }
        return res.intValue() == 0;
    }

    /*
     * @Description: query User by using activationCode
     * @Param: [code]
     * @return bookstore.user.domain.User
     **/
    public User findByActivationCode(String code) {
        String sql = "select * from user where activationCode=?";
        Object[] param = {code};
        User user = null;
        try {
            user = qr.query(sql,new BeanHandler<>(User.class),param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /*
     * @Description: update activation status by using uid and new status
     * @Param: [uid, status]
     * @return void
     **/
    public void updateStatus(String uid, boolean status) {
        String sql = "update user set status=? where uid=?";
        Object[] params = {status,uid};
        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description: find user by username
     * @Param: [username]
     * @return bookstore.user.domain.User
     **/
    public User findByUsernameAndPassword(String username,String password) {
        String sql = "select * from user where username=? and password=?";
        Object[] param = {username,password};
        User user = null;

        try {
            user = qr.query(sql,new BeanHandler<>(User.class), param);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("按用户名和密码查询用户失败");
        }
        return user;
    }

    /*
     * @Description: change password by uid
     * @Param: [uid, newpassword]
     * @return void
     **/
    public void changePassword(String uid, String newpassword) {
        String sql = "update user set password=? where uid=?";
        Object[] params = {newpassword,uid};
        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("修改密码失败");
        }
    }
}
