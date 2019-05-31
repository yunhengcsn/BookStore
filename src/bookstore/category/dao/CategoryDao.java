package bookstore.category.dao;

import bookstore.category.domain.Category;
import org.apache.commons.dbutils.handlers.MapListHandler;
import tools.commons.CommonUtils;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.category.dao
 * @Author: csn
 * @Description: dao layer of Category
 */
public class CategoryDao {
    private TxQueryRunner qr = new TxQueryRunner();

    /**
     * @Description: find categories with their respective subcategories
     * @Param: [pid]
     * @return java.util.List<bookstore.category.domain.Category>
     * @throws: SQLException
     */
    public List<Category> findCategories() throws SQLException {
        String sql;
        List<Category> categories;
        //search category
        sql = "select * from category where pid is null";

        //查出的数据缺少parent属性
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler());
        categories = toCategoryList(mapList);

        //search subcategory
        for(Category parent : categories ) {
            List<Category> children = findChildren(parent.getCid());
            parent.setChildren(children);
        }

        return categories;
    }

    /*
     * @Description:将map映射为Category对象
     * @Param: [map]
     * @return bookstore.category.domain.Category
     **/
    private Category toCategory(Map<String,Object> map) {
        //映射
        Category category = CommonUtils.toBean(map,Category.class);
        String pid = (String)map.get("pid");
        //设置parent属性
        if(pid != null) {
            //new 父分类
            Category parent = new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }
        return category;
    }

    /*
     * @Description: 将List<Map> 映射为List<Category>
     * @Param: [mapList]
     * @return java.util.List<bookstore.category.domain.Category>
     **/
    private List<Category> toCategoryList(List<Map<String,Object>> mapList)  {
        List<Category> categories = new ArrayList<>();
        for(Map<String,Object> map : mapList) {
            categories.add(toCategory(map));
        }
        return categories;
    }

    /*
     * @Description: find children by pid
     * @Param: [pid]
     * @return java.util.List<bookstore.category.domain.Category>
     **/
    private List<Category> findChildren(String pid) throws SQLException {
        String sql = "select * from category where pid=?";
        Object[] params = {pid};
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),params);
        return toCategoryList(mapList);
    }

}
