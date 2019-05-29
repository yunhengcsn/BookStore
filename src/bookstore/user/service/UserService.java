package bookstore.user.service;

import bookstore.user.dao.UserDao;
import bookstore.user.domain.User;
import tools.commons.CommonUtils;
import tools.mail.Mail;
import tools.mail.MailUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.service
 * @Author: csn
 * @Description: model of user module
 */
public class UserService {
    private UserDao userDao = new UserDao();

    /*
     * @Description: Complete form and store the information, then send activation email
     * @Param: [form]
     * @return void
     **/
    public void regist(User form) {
        //补全uid、status、activaitonCode
        form.setUid(CommonUtils.uuid());
        form.setStatus(false);
        form.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());

        //store form
        userDao.add(form);

        //dao层持久化数据后发送activation email
        Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取配置文件失败");
        }

        Mail mail = new Mail(props.getProperty("from"),form.getEmail());
        mail.setSubject(props.getProperty("subject"));
        String content = MessageFormat.format(props.getProperty("content"), form.getActivationCode());//将{0}用form.getActivationCode()替换
        mail.setContent(content);

        Session s = MailUtils.createSession(props.getProperty("host"),props.getProperty("username"),props.getProperty("password"));
        try {
            MailUtils.send(s,mail);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("发送邮件失败");
        }


    }

    /*
     * @Description: check if username has been registered.
     * @Param: [username]
     * @return boolean
     **/
    public boolean validateUsername(String username) {
        return userDao.findByName(username);
    }

    /*
     * @Description: check if email has been registered.
     * @Param: [email]
     * @return boolean
     **/
    public boolean validateEmail(String email) {
        return userDao.findByEmail(email);
    }

    /*
     * @Description: activation
     * @Param: [code]
     * @return void
     **/
    public void activation(String code) throws UserException {
        User user = userDao.findByActivationCode(code);
        if(user == null) {
            throw new UserException("激活码无效");
        } else if(user.getStatus()) {
            throw new UserException("您已经激活过了！");
        } else {
            user.setStatus(true);
            userDao.updateStatus(user.getUid(), user.getStatus());
        }
    }

    /**
     * @Description: user login
     * @Param: [form]
     * @return void
     **/
    public void login(User form) throws UserException {
        User user = userDao.findByUsername(form.getUsername());
        if(user == null || !form.getPassword().equals(user.getPassword())) {
            throw new UserException("用户名或密码错误！");
        }
    }
}
