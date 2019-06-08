package bookstore.category.service;

import bookstore.book.dao.BookDao;
import bookstore.category.dao.CategoryDao;
import bookstore.category.domain.Category;
import tools.jdbc.JdbcUtils;

import javax.sql.rowset.JdbcRowSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.category.service
 * @Author: csn
 * @Description: Service layer of Category
 */
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    private BookDao bookDao = new BookDao();

    /*
     * @Description: find all categories 事务
     * @Param: []
     * @return java.util.List<bookstore.category.domain.Category>
     **/
    public List<Category> findAll() {
        //找出分类
        List<Category> categories = null;
        try {
            JdbcUtils.beginTransaction();
            categories = categoryDao.findCategories();
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * add first level category
     * @param category
     */
    public void addParent(Category category) {
        try {
            categoryDao.addCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * add second level category
     * @param child
     */
    public void addChild(Category child) {
        try {
            categoryDao.addCategory(child);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * find all the first level categories
     * @return List<Category>
     */
    public List<Category> findAllParents() {
        List<Category> parents = null;
        try {
            parents = categoryDao.findAllParents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

    /**
     * find category by cid 事务
     * @param cid
     * @return
     */
    public Category findByCid(String cid) {
        Category category = null;
        try {
            JdbcUtils.beginTransaction();
            category = categoryDao.findByCid(cid);
            JdbcUtils.commitTransaction();
        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return category;
    }

    /**
     * edit category
     * @param form
     */
    public void editCategory(Category form) {
        try {
            categoryDao.editCategory(form);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete category
     * @param cid
     */
    public void deleteCategory(String cid) {
        try {
            categoryDao.deleteCategory(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countBooksByCid(String cid) {
        int num = -1;
        try {
            num = bookDao.countBooksByCid(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
}
