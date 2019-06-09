package bookstore.admin.book.servlet;

import bookstore.book.domain.Book;
import bookstore.book.service.BookService;
import bookstore.category.domain.Category;
import bookstore.category.service.CategoryService;
import bookstore.paging.PageBean;
import org.omg.CORBA.PRIVATE_MEMBER;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Description:
 *
 * @author csn
 */
public class AdminBookServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();
    private BookService bookService = new BookService();

    /**
     * load categories in left.jsp
     * @param req
     * @param resp
     * @return String
     */
    public String loadCategories(HttpServletRequest req, HttpServletResponse resp) {
        List<Category> categoryList = categoryService.findAll();
        req.setAttribute("categoryList",categoryList);
        return "f:/adminjsps/admin/book/left.jsp";
    }

    /**
     * load books of a given category and show with paging
     * @param req
     * @param resp
     * @return String
     */
    public String loadBooks(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");
        int currPage = getCurrPage(req);
        String url = getUrl(req);

        PageBean<Book> bookPageBean = bookService.findBooksByCategory(cid,currPage);
        bookPageBean.setUrl(url);

        req.setAttribute("pageBean",bookPageBean);

        return "f:/adminjsps/admin/book/list.jsp";
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
     * load book details by bid
     * @param req
     * @param resp
     * @return String
     */
    public String loadBook(HttpServletRequest req, HttpServletResponse resp) {
        String bid = req.getParameter("bid");

        Book book = bookService.findByBid(bid);
        req.setAttribute("book",book);

        List<Category> categoryList = categoryService.findAll();//编辑或删除页面要显示一级分类
        req.setAttribute("categoryList",categoryList);

        for(Category parent : categoryList) {
            if(parent.getCid().equals(book.getCategory().getParent().getCid())) {
                req.setAttribute("children",parent.getChildren());
                break;
            }
        }

        return "f:/adminjsps/admin/book/desc.jsp";
    }

    /**
     * ajax find children by pid
     * @param req
     * @param resp
     * @return
     */
    public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pid = req.getParameter("pid");

        List<Category> categoryList = categoryService.findAll();
        List<Category> children = new ArrayList<>();

        for(Category parent : categoryList) {
            if(parent.getCid().equals(pid)) {
               children = parent.getChildren();
               break;
            }
        }
        String json = toJson(children);
        resp.getWriter().print(json);
        return null;
    }



    /**
     * turn Category into Json String :{"cid":"fdsafdsa", "cname":"fdsafdas"}
     * @param category
     * @return String
     */
    private String toJson(Category category) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
        sb.append(",");
        sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**
     * turn List<Category> into Json String : [{"cid":"fdsafdsa", "cname":"fdsafdas"}, {"cid":"fdsafdsa", "cname":"fdsafdas"}]
     * @param categoryList
     * @return String
     */
    private String toJson(List<Category> categoryList) {
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < categoryList.size(); i++) {
            sb.append(toJson(categoryList.get(i)));
            if(i < categoryList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * delete book
     * @param req
     * @param resp
     * @return String
     */
    public String delete(HttpServletRequest req, HttpServletResponse resp) {
        String bid = req.getParameter("bid");
        bookService.deleteBook(bid);

        req.setAttribute("code","success");
        req.setAttribute("msg","删除图书成功！");
        return "f:/adminjsps/msg.jsp";

    }

    /**
     * edit book
     * @param req
     * @param resp
     * @return String
     */
    public String edit(HttpServletRequest req, HttpServletResponse resp) {
        Book form = CommonUtils.toBean(req.getParameterMap(),Book.class);
        String pid = req.getParameter("pid");
        String cid = req.getParameter("cid");
        Category parent = new Category();
        parent.setCid(pid);
        Category category = new Category();
        category.setCid(cid);
        category.setParent(parent);

        form.setCategory(category);
        bookService.editBook(form);

        req.setAttribute("code","success");
        req.setAttribute("msg","编辑图书成功！");
        return "f:/adminjsps/msg.jsp";
    }

    /**
     * find books by author
     * @param req
     * @param resp
     * @return String
     */
    public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) {
        String author = req.getParameter("author");
        int currPage = getCurrPage(req);
        String url = getUrl(req);

        PageBean<Book> bookPageBean = bookService.findBooksByAuthor(author,currPage);

        bookPageBean.setUrl(url);
        req.setAttribute("pageBean",bookPageBean);
        return "f:/adminjsps/admin/book/list.jsp";

    }

    /**
     * find books by press
     * @param req
     * @param resp
     * @return String
     */
    public String findByPress(HttpServletRequest req, HttpServletResponse resp) {
        String press = req.getParameter("press");
        int currPage = getCurrPage(req);
        String url = getUrl(req);

        PageBean<Book> bookPageBean = bookService.findBooksByPress(press,currPage);

        bookPageBean.setUrl(url);
        req.setAttribute("pageBean",bookPageBean);
        return "f:/adminjsps/admin/book/list.jsp";

    }

    public String findByCriteria(HttpServletRequest req, HttpServletResponse resp) {
        Book criteria = CommonUtils.toBean(req.getParameterMap(),Book.class);
        int currPage = getCurrPage(req);
        String url = getUrl(req);

        PageBean<Book> bookPageBean = bookService.findBooksByCombination(criteria,currPage);

        bookPageBean.setUrl(url);
        req.setAttribute("pageBean",bookPageBean);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    //addBook

}
