package bookstore.admin.admin.domain;

/**
 * Description: model of admin
 *+-----------+-------------+------+-----+---------+-------+
 * | Field     | Type        | Null | Key | Default | Extra |
 * +-----------+-------------+------+-----+---------+-------+
 * | adminId   | char(32)    | NO   | PRI | NULL    |       |
 * | adminname | varchar(50) | YES  |     | NULL    |       |
 * | adminpwd  | varchar(50) | YES  |     | NULL    |       |
 * +-----------+-------------+------+-----+---------+-------+
 * @author csn
 */
public class Admin {
    private String adminId;
    private String adminname;
    private String adminpwd;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminpwd() {
        return adminpwd;
    }

    public void setAdminpwd(String adminpwd) {
        this.adminpwd = adminpwd;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", adminname='" + adminname + '\'' +
                ", adminpwd='" + adminpwd + '\'' +
                '}';
    }
}
