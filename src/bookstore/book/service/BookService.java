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

    /**
     * Description: find book detail by bid
     * @param bid
     * @return Book
     */
    public Book findByBid(String bid) {
        return bookDao.findBookByBid(bid);
    }

    /**
     * Description: find books by author
     * @param author
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByAuthor(String author, int currPage) {
        return bookDao.findBooksByAuthor(author,currPage);
    }

    /**
     * Description: find books by press
     * @param press
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByPress(String press, int currPage) {
        return bookDao.findBooksByPress(press,currPage);
    }

    /**
     * Description: fuzzy search bookList by bname
     * @param bname
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByBname(String bname, int currPage) {
        return  bookDao.findBooksByBname(bname,currPage);
    }

    /**
     * Description: fuzzy search bookList by combination conditions
     * @param criteria
     * @param currPage
     * @return PageBean<Book>
     */
    public PageBean<Book> findBooksByCombination(Book criteria, int currPage) {
        return bookDao.findBooksByCombination(criteria,currPage);
    }
}
