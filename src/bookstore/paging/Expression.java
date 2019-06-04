package bookstore.paging;

/**
 * Description: 表达式
 * @author csn
 */
public class Expression {
    private String name;//名称
    private String operator;//运算符
    private String value;//值

    public Expression() {
    }

    public Expression(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
