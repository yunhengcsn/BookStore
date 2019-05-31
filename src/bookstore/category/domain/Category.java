package bookstore.category.domain;

import java.util.List;

/**
 * @BelongsProject: BookStore
 * @BelongsPackage: bookstore.category.domain
 * @Author: csn
 * @Description: Category
 */
public class Category {
    private String cid;
    private String cname;
    private Category parent;
    private String desc; //description of the category
    private List<Category> children;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid='" + cid + '\'' +
                ", cname='" + cname + '\'' +
                ", parent='" + parent + '\'' +
                ", desc='" + desc + '\'' +
                ", children=" + children +
                '}';
    }
}
