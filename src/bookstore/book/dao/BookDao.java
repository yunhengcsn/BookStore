package bookstore.book.dao;

import bookstore.book.domain.Book;
import bookstore.category.domain.Category;
import bookstore.paging.Expression;
import bookstore.paging.PageBean;
import bookstore.paging.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import tools.commons.CommonUtils;
import tools.jdbc.TxQueryRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: dao layer of book
 * @author csn
 */
public class BookDao {

    private QueryRunner qr = new TxQueryRunner();

    /**
     * Description:find books by cid and currPage
     * @param cid
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByCategory(String cid, int currPage) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new Expression("cid","=", cid));
        return findBooksByCriteria(expressionList, currPage);
    }

    /**
     * Description: 查询方法
     * @param expressionList
     * @param currPage
     * @return PageBean<Book>
     */
    private PageBean<Book> findBooksByCriteria(List<Expression> expressionList, int currPage) {
        //每页记录数
        int pageSize = PageConstants.BOOK_PAGE_SIZE;

        StringBuilder whereSql = new StringBuilder("where ");//where条件
        List<Object> params = new ArrayList<>();//参数列表
        int size = expressionList.size();

        for(int i = 0; i < size; i++) {
            //第一个参数不加and
            if(i != 0) {
                whereSql.append(" and ");
            }
            //where条件拼接 column = ?等
            whereSql.append(expressionList.get(i).getName()).append(' ').append(expressionList.get(i).getOperator());

            //若运算符不为is null，则加参数和？;否则pass
            if(! expressionList.get(i).getOperator().equals("is null")) {
                whereSql.append(" ?");
                params.add(expressionList.get(i).getValue());
            }
        }

        //查询总记录数，赋值给pageBean属性
        Number totalRecords;
        String sql = "select count(*) from book " + whereSql;
        try {
            totalRecords = (Number) qr.query(sql,new ScalarHandler(),params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询总记录数失败");
        }


        //按条件查询bookList
        List<Book> bookList;
        sql = "select * from book " + whereSql + " limit ?,?";
        params.add((currPage-1) * pageSize);
        params.add(pageSize);
        try {
            bookList = qr.query(sql,new BeanListHandler<>(Book.class), params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败");
        }

        //设置pageBean,url由servlet设置
        PageBean<Book> pageBean = new PageBean<>();

        pageBean.setBeanList(bookList);
        pageBean.setCurrPage(currPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalRecords(totalRecords.intValue());

        return pageBean;
    }

    /**
     * Description: find book by Bid
     * @param bid
     * @return Book
     */
    public Book findBookByBid(String bid) {
        String sql = "select * from book b , category c where b.cid=c.cid and bid=?";
        Object[] params = {bid};
        Map<String,Object> map;
        try {
            map = qr.query(sql,new MapHandler(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询图书失败");
        }

        //查询到的与book相关的列映射为Book对象
        Book book = CommonUtils.toBean(map,Book.class);
        //查询到的与category相关的列映射为Category对象
        Category category = CommonUtils.toBean(map,Category.class);
        //设置category的parent属性，只放pid
        if(map.get("pid") != null) {
            Category p = new Category();
            p.setCid((String) map.get("pid"));
            category.setParent(p);
        }

        //设置book的category属性
        book.setCategory(category);
        return book;
    }

    /**
     * Description: find books by author
     * @param author
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByAuthor(String author, int currPage) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new Expression("author","=",author));

        return findBooksByCriteria(expressionList,currPage);
    }

    /**
     * Description: find books by press
     * @param press
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByPress(String press, int currPage) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new Expression("press","=",press));

        return findBooksByCriteria(expressionList,currPage);
    }

    /**
     * Description: fuzzy search bookList by bname
     * @param bname
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByBname(String bname, int currPage) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(new Expression("bname","like",'%'+bname+'%'));

        return findBooksByCriteria(expressionList,currPage);
    }

    /**
     * Description: fuzzy search bookList by combination conditions
     * @param criteria
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByCombination(Book criteria, int currPage) {
        List<Expression> expressionList = new ArrayList<>();
        if(criteria.getBname() != null) {
            expressionList.add(new Expression("bname","like",'%'+criteria.getBname()+'%'));
        }
        if(criteria.getAuthor() != null) {
            expressionList.add(new Expression("author","like",'%'+criteria.getAuthor()+'%'));
        }
        if(criteria.getPress() != null) {
            expressionList.add(new Expression("press","like",'%'+criteria.getPress()+'%'));
        }

        return findBooksByCriteria(expressionList,currPage);
    }

    /**
     * count books under a given category
     * @param cid
     * @return int
     * @throws SQLException
     */
    public int countBooksByCid(String cid) throws SQLException {
        String sql = "select count(*) from book where cid=?";

        Number num =  (Number)qr.query(sql,new ScalarHandler(),cid);
        return num.intValue();
    }

    /**
     * delete book
     * @param bid
     * @throws SQLException
     */
    public void deleteBook(String bid) throws SQLException {
        String sql = "delete from book where bid=?";
        qr.update(sql,bid);
    }

    /**
     * edit book
     * @param book
     * @throws SQLException
     */
    public void editBook(Book book) throws SQLException {
        StringBuilder sql = new StringBuilder("update book set bname=?, currPrice=?, price=?, discount=?,author=?,press=?,cid=? ");
        ArrayList<Object> params = new ArrayList<>();
        params.add(book.getBname());
        params.add(book.getCurrPrice());
        params.add(book.getPrice());
        params.add(book.getDiscount());
        params.add(book.getAuthor());
        params.add(book.getPress());
        params.add(book.getCategory().getCid());
        /*
        <tr>
				<td colspan="3">出版时间：<input id="publishtime" type="text" name="publishtime" value="${book.publishtime}" style="width:100px;"/></td>
			</tr>
			<tr>
				<td>版次：　　<input id="edition" type="text" name="edition" value="${book.edition}" style="width:40px;"/></td>
				<td>页数：　　<input id="pageNum" type="text" name="pageNum" value="${book.pageNum}" style="width:50px;"/></td>
				<td>字数：　　<input id="wordNum" type="text" name="wordNum" value="${book.wordNum}" style="width:80px;"/></td>
			</tr>
			<tr>
				<td width="250px">印刷时间：<input id="printtime" type="text" name="printtime" value="${book.printtime}" style="width:100px;"/></td>
				<td width="250px">开本：　　<input id="booksize" type="text" name="booksize" value="${book.booksize}" style="width:30px;"/></td>
				<td>纸张：　　<input id="paper" type="text" name="paper" value="${book.paper}" style="width:80px;"/></td>
			</tr>
         */
        if(book.getPublishtime() != null) {
            sql.append(",publishtime=? ");
            params.add(book.getPublishtime());
        }
        if((Integer)book.getEdition() != null) {
            sql.append(",edition=? ");
            params.add(book.getEdition());
        }
        if((Integer)book.getPageNum() != null) {
            sql.append(",pageNum=? ");
            params.add(book.getPageNum());
        }
        if((Integer)book.getWordNum() != null) {
            sql.append(",wordNum=? ");
            params.add(book.getWordNum());
        }
        if(book.getPrinttime() != null) {
            sql.append(",printtime=? ");
            params.add(book.getPrinttime());
        }
        if((Integer)book.getBooksize() != null) {
            sql.append(",booksize=? ");
            params.add(book.getBooksize());
        }
        if(book.getPaper() != null) {
            sql.append(",paper=? ");
            params.add(book.getPaper());
        }

        sql.append("where bid=?");
        params.add(book.getBid());

        qr.update(sql.toString(),params.toArray());
    }
}
