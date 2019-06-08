package bookstore.admin.admin.servlet;

import bookstore.admin.admin.domain.Admin;
import bookstore.admin.admin.service.AdminService;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Description:
 *
 * @author csn
 */
public class AdminServlet extends BaseServlet {
    private AdminService adminService = new AdminService();

    /**
     * 管理员登录：校验，转发
     * @param req
     * @param resp
     * @return String
     */
    public String login(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        Admin form = CommonUtils.toBean(req.getParameterMap(),Admin.class);

        Admin admin = adminService.findAdmin(form.getAdminname(),form.getAdminpwd());

        if(admin == null) {
            req.setAttribute("msg","管理员名或密码错误");
            req.setAttribute("form",form);
            return "f:/adminjsps/login.jsp";
        }

        //put admin into session
        req.getSession().setAttribute("sessionAdmin",admin);

        //put admin into cookie
        String adminname = admin.getAdminname();
        adminname = URLEncoder.encode(adminname,"utf-8");
        Cookie cookie = new Cookie("adminname",adminname);
        cookie.setMaxAge(60 * 60 * 24 * 10);
        resp.addCookie(cookie);

        return "f:/adminjsps/admin/index.jsp";
    }

    /**
     * 管理员退出：销毁session，重定向
     * @param req
     * @param resp
     * @return String
     */
    public String exit(HttpServletRequest req, HttpServletResponse resp) {

        req.getSession().invalidate();
        return "r:/adminjsps/login.jsp";
    }
}
