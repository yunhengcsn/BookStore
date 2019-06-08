package bookstore.admin.category.sevlet;

import bookstore.category.domain.Category;
import bookstore.category.service.CategoryService;
import tools.commons.CommonUtils;
import tools.servlet.BaseServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Description:
 *
 * @author csn
 */
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();

    /**
     * find all categories
     * @param req
     * @param resp
     * @return
     */
    public String findAll(HttpServletRequest req, HttpServletResponse resp) {

        req.setAttribute("categoryList",categoryService.findAll());
        return "f:/adminjsps/admin/category/list.jsp";
    }

    /**
     * add first level category
     * @param req
     * @param resp
     * @return
     */
    public String addFirstLevel(HttpServletRequest req, HttpServletResponse resp) {
        Category category = CommonUtils.toBean(req.getParameterMap(),Category.class);

        category.setParent(null);
        category.setCid(CommonUtils.uuid());

        categoryService.addParent(category);
        return findAll(req,resp);
    }

    /**
     * load all categories
     * @param req
     * @param resp
     * @return
     */
    public String preAddSecondLevel(HttpServletRequest req, HttpServletResponse resp) {

        req.setAttribute("parents",categoryService.findAllParents());
        return "f:/adminjsps/admin/category/add2.jsp";
    }
    /**
     * add second level category belong to cid
     * @param req
     * @param resp
     * @return
     */
    public String addSecondLevel(HttpServletRequest req, HttpServletResponse resp) {
        Category child = CommonUtils.toBean(req.getParameterMap(),Category.class);
        child.setCid(CommonUtils.uuid());

        String pid = req.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);

        categoryService.addChild(child);
        return findAll(req,resp);
    }

    /**
     * load info of category identified by cid
     * @param req
     * @param resp
     * @return
     */
    public String preEdit(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");

        Category category = categoryService.findByCid(cid);

        req.setAttribute("category",category);
        req.setAttribute("parents",categoryService.findAllParents());

        if(category.getParent() != null) {
            return "f:/adminjsps/admin/category/edit2.jsp";
        } else {
            return "f:/adminjsps/admin/category/edit.jsp";
        }
    }

    /**
     * edit category identified by cid:  cid,cname,desc
     * @param req
     * @param resp
     * @return
     */
    public String editFirstLevel(HttpServletRequest req, HttpServletResponse resp) {
        Category form = CommonUtils.toBean(req.getParameterMap(),Category.class);

        categoryService.editCategory(form);
        return findAll(req,resp);
    }

    /**
     * edit category identified by cid
     * @param req
     * @param resp
     * @return
     */
    public String editSecondLevel(HttpServletRequest req, HttpServletResponse resp) {
        Category form = CommonUtils.toBean(req.getParameterMap(),Category.class);

        String pid = req.getParameter("pid");
        Category p = new Category();
        p.setCid(pid);
        form.setParent(p);

        categoryService.editCategory(form);
        return findAll(req,resp);
    }

    /**
     * delete first level Category having no child
     * @param req
     * @param resp
     * @return
     */
    public String deleteFirstLevel(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");
        Category category = categoryService.findByCid(cid);

        if(category.getChildren() != null ){
            req.setAttribute("msg","该分类下仍有二级分类，不能删除！");
            req.setAttribute("code","error");
            return "f:/adminjsps/msg.jsp";
        }

        categoryService.deleteCategory(cid);
        return findAll(req,resp);
    }


    /**
     * delete secondlevel Category having no book
     * @param req
     * @param resp
     * @return
     */
    public String deleteSecondLevel(HttpServletRequest req, HttpServletResponse resp) {
        String cid = req.getParameter("cid");
        int num = categoryService.countBooksByCid(cid);
        if(num != 0) {
            req.setAttribute("msg","该分类下仍有图书，不能删除！");
            req.setAttribute("code","error");
            return "f:/adminjsps/msg.jsp";
        }
        categoryService.deleteCategory(cid);
        return findAll(req,resp);
    }

}
