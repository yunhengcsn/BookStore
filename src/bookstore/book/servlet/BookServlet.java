package bookstore.book.servlet;

import bookstore.book.domain.Book;
import bookstore.book.service.BookService;
import bookstore.paging.PageBean;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Description: web layer of book
 * @author csn
 */
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookService();

    /**
     * Description: find PageBean<Book> by cid and currPage
     * @param req resp
     * @return String
     */
    public String findByCategory(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);

        String url = getUrl(req);

        String cid = req.getParameter("cid");

        //获取PageBean<Book>,设置url
        PageBean<Book> bookPageBean = bookService.findBooksByCategory(cid,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("bookPageBean",bookPageBean);

        return "f:/jsps/book/list.jsp";
    }

    /**
     * Description: 去除url的currPage参数
     * @param req
     * @return String
     */
    private String getUrl(HttpServletRequest req) {
        String url = req.getRequestURI() + "?" + req.getQueryString();

        int id = url.lastIndexOf("&currPage=");
        if(id != -1) url = url.substring(0,id);
        return url;
    }

    /**
     * Description: get currPage from req
     * @param req
     * @return int
     */
    private int getCurrPage(HttpServletRequest req) {
        String currPage = req.getParameter("currPage");

        //req中没有参数则为1，有则转换为int
        if(currPage == null || currPage.trim().isEmpty()) return 1;
        else return Integer.parseInt(currPage);
    }

    /**
     * Description: search book details by bid
     * @param req resp
     * @return String
     */
    public String findByBid(HttpServletRequest req, HttpServletResponse resp) {
        String bid = req.getParameter("bid");

        Book book = bookService.findByBid(bid);

        req.setAttribute("book",book);
        return "f:/jsps/book/desc.jsp";
    }

    /**
     * Description: search bookList by author
     * @param req
     * @param resp
     * @return String
     */
    public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);
        String url = getUrl(req);
        String author = req.getParameter("author");

        PageBean<Book> bookPageBean = bookService.findBooksByAuthor(author,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("bookPageBean",bookPageBean);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * Description: search bookList by press
     * @param req
     * @param resp
     * @return String
     */
    public String findByPress(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);
        String url = getUrl(req);
        String press = req.getParameter("press");

        PageBean<Book> bookPageBean = bookService.findBooksByPress(press,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("bookPageBean",bookPageBean);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * Description: fuzzy search bookList by bname
     * @param req
     * @param resp
     * @return String
     */
    public String findByBname(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);
        String url = getUrl(req);
        String bname = req.getParameter("bname");

        PageBean<Book> bookPageBean = bookService.findBooksByBname(bname,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("bookPageBean",bookPageBean);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * Description: fuzzy search bookList by combination conditions
     * @param req
     * @param resp
     * @return String
     */
    public String findByCombination(HttpServletRequest req, HttpServletResponse resp) {
        int currPage = getCurrPage(req);
        String url = getUrl(req);
        Book criteria = CommonUtils.toBean(req.getParameterMap(),Book.class);

        PageBean<Book> bookPageBean = bookService.findBooksByCombination(criteria,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("bookPageBean",bookPageBean);
        return "f:/jsps/book/list.jsp";
    }

}
