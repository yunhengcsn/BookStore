package bookstore.user.servlet;

import bookstore.user.domain.User;
import bookstore.user.service.UserException;
import bookstore.user.service.UserService;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.servlet
 * @Author: csn
 * @CreateTime: 2019-05-23 15:10
 * @Description: web layer of user module
 */
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    /*
     * @Description: 用户注册功能
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String regist(HttpServletRequest req, HttpServletResponse resp) {
        User form = CommonUtils.toBean(req.getParameterMap(), User.class);

        Map<String,String> errors = validateRegist(form,req.getSession());
        if(errors.size() != 0) {
            req.setAttribute("form",form);
            req.setAttribute("errors",errors);
            return "f:/jsps/user/regist.jsp";
        }

        userService.regist(form);
        req.setAttribute("msg","请前往邮箱激活完成注册！");
        req.setAttribute("code","success");
        return "f:/jsps/msg.jsp";
    }

    /*
     * @Description: 注册表单校验，得到错误map
     * @Param: [form, session]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    private Map<String, String> validateRegist(User form, HttpSession session) {
        String username = form.getUsername();
        String password = form.getPassword();
        String repassword = form.getRepassword();
        String email = form.getEmail();
        String verifyCode = form.getVerifyCode();

        Map<String,String> errors = new HashMap<>();

        //校验用户名
        if(username == null || username.trim().isEmpty()) {
            errors.put("usernameError","用户名不能为空");
        } else if(username.length() < 3 || username.length() > 15) {
            errors.put("usernameError","用户名长度必须在3到15之间");
        }else if(!userService.validateUsername(username)) {
            errors.put("usernameError","用户名已注册");
        }

        //校验密码
        if(password == null || password.trim().isEmpty()) {
            errors.put("passwordError","密码不能为空");
        } else if(password.length() < 3 || password.length() > 15) {
            errors.put("passwordError","密码长度必须在3到15之间");
        }

        //校验二次输入的密码
        if(repassword == null || repassword.trim().isEmpty()) {
            errors.put("repasswordError","校验密码不能为空");
        }else if(!repassword.equals(password)) {
            errors.put("repasswordError","两次密码输入不一致");
        }

        //校验邮箱
        if(email == null || email.trim().isEmpty()) {
            errors.put("emailError","邮箱不能为空");
        }else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            errors.put("emailError","邮箱地址有误");
        }else if(!userService.validateEmail(email)) {
            errors.put("emailError","邮箱已注册");
        }

        //校验验证码
        if(verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCodeError","验证码不能为空");
        }else if (! verifyCode.equals(session.getAttribute("verifyCode"))) {
            errors.put("verifyCodeError","验证码输入错误");
        }

        return errors;
    }

    public String ajaxValidateUsername() {
        return null;
    }
    public String ajaxValidateEmail() {
        return null;
    }
    public String ajaxValidateVerifyCode(){
        return null;
    }

    /*
     * @Description: activate user
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String activation(HttpServletRequest req, HttpServletResponse resp) {
        String code = req.getParameter("activationCode");
        if(code == null || code.trim().isEmpty()) {
            req.setAttribute("msg","激活码不存在");
        } else {
            try {
                userService.activation(code);
                req.setAttribute("msg","完成激活！");
                req.setAttribute("code","success");
            } catch (UserException e) {
                req.setAttribute("msg",e.getMessage());
                req.setAttribute("code","error");
            }
        }
        return "f:/jsps/msg.jsp";
    }
}
