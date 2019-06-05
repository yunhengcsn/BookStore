package bookstore.book.domain;

import bookstore.category.domain.Category;

/**
 * Description: model of book
 *
 * @author csn
 */

/* 数据库表结构：
 +-------------+--------------+------+-----+---------+----------------+
 | Field       | Type         | Null | Key | Default | Extra          |
 +-------------+--------------+------+-----+---------+----------------+
 | bid         | char(32)     | NO   | PRI | NULL    |                |
 | bname       | varchar(200) | YES  |     | NULL    |                |
 | author      | varchar(50)  | YES  |     | NULL    |                |
 | price       | decimal(8,2) | YES  |     | NULL    |                |
 | currPrice   | decimal(8,2) | YES  |     | NULL    |                |
 | discount    | decimal(3,1) | YES  |     | NULL    |                |
 | press       | varchar(100) | YES  |     | NULL    |                |
 | publishtime | char(10)     | YES  |     | NULL    |                |
 | edition     | int(11)      | YES  |     | NULL    |                |
 | pageNum     | int(11)      | YES  |     | NULL    |                |
 | wordNum     | int(11)      | YES  |     | NULL    |                |
 | printtime   | char(10)     | YES  |     | NULL    |                |
 | booksize    | int(11)      | YES  |     | NULL    |                |
 | paper       | varchar(50)  | YES  |     | NULL    |                |
 | cid         | char(32)     | YES  | MUL | NULL    |                |
 | image_w     | varchar(100) | YES  |     | NULL    |                |
 | image_b     | varchar(100) | YES  |     | NULL    |                |
 | orderBy     | int(11)      | NO   | MUL | NULL    | auto_increment |
 +-------------+--------------+------+-----+---------+----------------+
 */
public class Book {
    private String bid;
    private String bname;//图书名
    private String author;//作者
    private double price;//定价
    private double currPrice;//当前价格
    private double discount;//折扣
    private String press;//出版社
    private String publishtime;//出版时间
    private int edition;//版本
    private int pageNum;//总页数
    private int wordNum;//总字数
    private String printtime;//印刷时间
    private int booksize;//纸张大小
    private String paper;//纸质

    private Category category;//图书所属分类

    private String image_w;//大图路径
    private String image_b;//小图路径

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public String getPrinttime() {
        return printtime;
    }

    public void setPrinttime(String printtime) {
        this.printtime = printtime;
    }

    public int getBooksize() {
        return booksize;
    }

    public void setBooksize(int booksize) {
        this.booksize = booksize;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImage_w() {
        return image_w;
    }

    public void setImage_w(String image_w) {
        this.image_w = image_w;
    }

    public String getImage_b() {
        return image_b;
    }

    public void setImage_b(String image_b) {
        this.image_b = image_b;
    }
}
