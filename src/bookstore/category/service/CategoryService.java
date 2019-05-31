package bookstore.category.service;

import bookstore.category.dao.CategoryDao;
import bookstore.category.domain.Category;

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

    /*
     * @Description: find all categories
     * @Param: []
     * @return java.util.List<bookstore.category.domain.Category>
     **/
    public List<Category> findAll() {
        //找出分类
        List<Category> categories ;
        try {
            categories = categoryDao.findCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询分类失败！");
        }
        return categories;
    }
}
