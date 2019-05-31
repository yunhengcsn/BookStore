package bookstore.category.servlet;

import bookstore.category.domain.Category;
import bookstore.category.service.CategoryService;
import tools.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.category.servlet
 * @Author: csn
 * @Description: web layer of Category
 */
public class CategoryServlet extends BaseServlet {

    private CategoryService categoryService = new CategoryService();

    /*
     * @Description: find all categories including subcategories
     * @Param: [req, res]
     * @return java.lang.String
     **/
    public String findAll(HttpServletRequest req, HttpServletResponse res){
        List<Category> categories = categoryService.findAll();

        req.setAttribute("categories",categories);

        return "f:/jsps/left.jsp";
    }
}
