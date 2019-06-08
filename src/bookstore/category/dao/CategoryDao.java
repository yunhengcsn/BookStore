package bookstore.category.dao;

import bookstore.category.domain.Category;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
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

    /**
     * add category
     * @param category
     * @throws SQLException
     */
    public void addCategory(Category category) throws SQLException {
        StringBuilder sql = new StringBuilder("insert into category (cid,cname,`desc`") ;
                //"pid) values(?,?,?,?)";
        ArrayList<Object> params = new ArrayList<>();
        params.add(category.getCid());
        params.add(category.getCname());
        params.add(category.getDesc());

        String sql2;
        if(category.getParent() != null) {
            params.add(category.getParent().getCid());//已有pid
            sql2 = ",pid) values (?,?,?,?)";
        } else {
            sql2 = ") values (?,?,?)";
        }

        qr.update(sql.append(sql2).toString(),params.toArray());
    }

    /**
     * find categories whose pid is null
     * @return List<Category>
     */
    public List<Category> findAllParents() throws SQLException {
        String sql = "select * from category where pid is null";

        return qr.query(sql,new BeanListHandler<>(Category.class));
    }

    /**
     * find category by cid
     * @param cid
     * @return
     * @throws SQLException
     */
    public Category findByCid(String cid) throws SQLException {
        String sql;
        Category category;
        //search category
        sql = "select * from category where cid=?";

        //查出的数据缺少parent属性
        Map<String,Object> map = qr.query(sql,new MapHandler(),cid);
        category = toCategory(map);

        //一级分类设置children属性
        if(category.getParent() == null) {
            List<Category> children = findChildren(category.getCid());
            category.setChildren(children);
        }


        return category;
    }

    /**
     * update category
     * @param category
     * @throws SQLException
     */
    public void editCategory(Category category) throws SQLException {
        StringBuilder sql = new StringBuilder("update category set cname=? , `desc`=? ");
                //, pid=? where cid=?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(category.getCname());
        params.add(category.getDesc());

        String sql2;
        if(category.getParent() != null) {
            params.add(category.getParent().getCid());//已有pid
            sql2 = ", pid=? where cid=?";
        } else {
            sql2 = "where cid=?";
        }
        params.add(category.getCid());

        qr.update(sql.append(sql2).toString(),params.toArray());
    }

    public void deleteCategory(String cid) throws SQLException {
        String sql = "delete from category where cid=?";
        qr.update(sql,cid);
    }
}
