package bookstore.paging;

import java.util.List;

/**
 * Description: 分页Bean
 *
 * @author csn
 */
public class PageBean<T> {
    private int currPage;//当前页码
    private int totalRecords;//总记录数
    private int pageSize;//每页记录数
    private String url;//请求路径

    private List<T> beanList;

    public int getTotalPages() {//计算总页数
        int totalPages = totalRecords / pageSize;
        return totalRecords % pageSize == 0 ? totalPages : totalPages+1;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }
}
