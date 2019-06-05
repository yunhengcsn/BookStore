package bookstore.book.service;

import bookstore.book.dao.BookDao;
import bookstore.book.domain.Book;
import bookstore.paging.PageBean;


/**
 * Description: service layer of book
 * @author csn
 */
public class BookService {
    private BookDao bookDao = new BookDao();

    /**
     * Description: find books by cid and currPage
     * @param  cid
     * @param  currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByCategory(String cid, int currPage) {
        return bookDao.findBooksByCategory(cid, currPage);
    }
}
