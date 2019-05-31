package bookstore.user.servlet;

import bookstore.user.domain.User;
import bookstore.user.service.UserException;
import bookstore.user.service.UserService;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.user.servlet
 * @Author: csn
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

    /*
     * @Description: ajax校验用户名是否注册
     * @Param: []
     * @return java.lang.String
     **/
    public String ajaxValidateUsername(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        boolean b = userService.validateUsername(username);
        try {
            resp.getWriter().print(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @Description: ajax校验邮箱是否注册
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
         * 1. 获取Email
         */
        String email = req.getParameter("email");
        /*
         * 2. 通过service得到校验结果
         */
        boolean b = userService.validateEmail(email);
        /*
         * 3. 发给客户端
         */
        resp.getWriter().print(b);
        return null;
    }
    
    /*
     * @Description: ajax校验验证码是否正确
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String verifyCode = req.getParameter("verifyCode");
        String vcCode = (String)req.getSession().getAttribute("verifyCode");
        boolean b = verifyCode.equals(vcCode);
        resp.getWriter().print(b);
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

    /*
     * @Description: user login
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String login(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        User form = CommonUtils.toBean(req.getParameterMap(),User.class);
        //check form data
        Map<String,String> errors = validateLogin(form,req.getSession());

        //表单校验不通过
        if(errors.size() != 0) {
            req.setAttribute("errors",errors);
            req.setAttribute("form",form);
            return "f:/jsps/user/login.jsp";
        }

        User user = userService.login(form);

        if(user == null) {
            req.setAttribute("submitError","用户名或密码错误");
            req.setAttribute("form",form);
            return "f:/jsps/user/login.jsp";
        } else if(!user.getStatus()) {
            req.setAttribute("submitError","您还没有激活！");
            req.setAttribute("form",form);
            return "f:/jsps/user/login.jsp";
        }

        //登录成功后向session存储用户信息
        req.getSession().setAttribute("sessionUser",user);

        //将用户名保存至cookie，使得用户下次登录时不用输入用户名
        String username = user.getUsername();
        username = URLEncoder.encode(username,"utf-8");
        Cookie cookie = new Cookie("username",username);
        cookie.setMaxAge(60 * 60 * 24 * 10);
        resp.addCookie(cookie);

        return "r:/index.jsp";
    }

    /*
     * @Description: check whether form is valid
     * @Param: [form, session]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    private Map<String, String> validateLogin(User form, HttpSession session) {
        String username = form.getUsername();
        String password = form.getPassword();
        String verifyCode = form.getVerifyCode();

        Map<String,String> errors = new HashMap<>();

        //check password
        if(password == null || password.trim().isEmpty()) {
            errors.put("passwordError","密码不能为空");
        } else if(password.length() < 3 || password.length() > 15) {
            errors.put("passwordError","密码长度必须在3到15之间");
        }

        //check username
        if(username == null || username.trim().isEmpty()) {
            errors.put("usernameError","用户名不能为空");
        } else if (username.length() < 3 || username.length() > 15) {
            errors.put("usernameError","用户名长度必须在3到15之间");
        }

        //check verifyCode
        if(verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCodeError","验证码不能为空");
        }else if (! verifyCode.equals(session.getAttribute("verifyCode"))) {
            errors.put("verifyCodeError","验证码输入错误");
        }

        return errors;
    }

   /*
    * @Description: user exit
    * @Param: [req, resp]
    * @return java.lang.String
    **/
    public String exit(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        return "r:/jsps/user/login.jsp";
    }

    /*
     * @Description: change password
     * @Param: [req, resp]
     * @return java.lang.String
     **/
    public String changePassword(HttpServletRequest req, HttpServletResponse resp) {
        User form = CommonUtils.toBean(req.getParameterMap(),User.class);

        //若用户未登录
        User sessionUser = (User)req.getSession().getAttribute("sessionUser");
        if(sessionUser == null) {
            req.setAttribute("msg","您还没有登录<br><a href=\"/BookStore_war_exploded/jsps/user/login.jsp\">点击登录</a>");
            req.setAttribute("code","error");
            return "f:/jsps/msg.jsp";

        }

        //校验表单
        Map<String,String> errors = validateChangePassword(form,req.getSession());
        if(errors.size() != 0) {
            req.setAttribute("errors",errors);
            req.setAttribute("form",form);
            return "f:/jsps/user/pwd.jsp";
        }

        //调用service方法
        userService.changePassword(sessionUser.getUid(),form.getNewpassword());

        req.getSession().invalidate();
        req.setAttribute("msg","密码修改成功！<br><a href=\"/BookStore_war_exploded/jsps/user/login.jsp\">点击登录</a>");
        req.setAttribute("code","success");
        return "f:/jsps/msg.jsp";
    }

    /*
     * @Description: check form
     * @Param: [form, session]
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    private Map<String, String> validateChangePassword(User form, HttpSession session) {
        String password = form.getPassword();
        String newpassword = form.getNewpassword();
        String repassword = form.getRepassword();
        String verifyCode = form.getVerifyCode();

        Map<String,String> errors = new HashMap<>();

        User sessionUser = (User)session.getAttribute("sessionUser");
        String sessionPassword = sessionUser.getPassword();

        //check old password
        if(password == null || password.trim().isEmpty()) {
            errors.put("passwordError","原密码不能为空");
        } else if(password.length() < 3 || password.length() > 15) {
            errors.put("passwordError","原密码长度必须在3到15之间");
        }
        //check whether old password is right
        else if(!password.equals(sessionPassword)) {
            errors.put("passwordError","原密码错误");
        }

        //check new password
        if(newpassword == null || newpassword.trim().isEmpty()) {
            errors.put("newpasswordError","新密码不能为空");
        } else if(newpassword.length() < 3 || newpassword.length() > 15) {
            errors.put("newpasswordError","新密码长度必须在3到15之间");
        }

        //check repassword
        if(repassword == null || repassword.trim().isEmpty()) {
            errors.put("repasswordError","校验密码不能为空");
        } else if(!repassword.equals(newpassword)) {
            errors.put("repasswordError","2次密码输入不一致");
        }

        //check verifyCode
        if(verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCodeError","验证码不能为空");
        }else if (! verifyCode.equals(session.getAttribute("verifyCode"))) {
            errors.put("verifyCodeError","验证码输入错误");
        }

        return errors;
    }
}
