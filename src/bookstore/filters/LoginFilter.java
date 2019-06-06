package bookstore.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Description:
 *
 * @author csn
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Description: forbid users that haven't logged in
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        if(req.getSession().getAttribute("sessionUser") == null) {
            req.setAttribute("msg","您还没有登录<br><a href=\"/BookStore_war_exploded/jsps/user/login.jsp\">点击登录</a>");
            req.setAttribute("code","error");
            req.getRequestDispatcher("/jsps/msg.jsp").forward(req,servletResponse);
        } else {
            filterChain.doFilter(req,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
