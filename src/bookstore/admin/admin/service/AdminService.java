package bookstore.admin.admin.service;

import bookstore.admin.admin.dao.AdminDao;
import bookstore.admin.admin.domain.Admin;

import java.sql.SQLException;

/**
 * Description:
 *
 * @author csn
 */
public class AdminService {
    private AdminDao adminDao = new AdminDao();

    /**
     * find admin by name and pwd
     * @param adminname
     * @param adminpwd
     * @return Admin
     */
    public Admin findAdmin(String adminname, String adminpwd) {
        Admin admin ;
        try {
            admin = adminDao.findAdmin(adminname,adminpwd);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询管理员信息失败");
        }
        return admin;
    }
}
