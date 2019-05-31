package bookstore.book.servlet;

import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.book.servlet
 * @Author: csn
 * @Description: BookServlet
 */
public class BookServlet extends BaseServlet {


    public String findByCategory(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");

        return "f:/jsps/book/list.jsp";
    }
}
