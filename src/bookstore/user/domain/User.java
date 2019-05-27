package bookstore.user.domain;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.domain
 * @Author: csn
 * @CreateTime: 2019-05-23 15:11
 * @Description: model of user module
 */
public class User {
    private String uid;
    private String username;
    private String password;
    private String email;
    private boolean status;//是否激活
    private String activationCode;

    private String repassword;
    private String verifyCode;

    private String newpassword;

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activaitonCode) {
        this.activationCode = activaitonCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", activationCode='" + activationCode + '\'' +
                ", repassword='" + repassword + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", newpassword='" + newpassword + '\'' +
                '}';
    }
}
