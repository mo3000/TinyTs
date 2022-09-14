package indi.suiwenbo.TinyTs.Ast;

public class LiteralValue {
    private ExprType type;

    public ExprType getType() {
        return type;
    }

    public void setType(ExprType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public LiteralValue(ExprType type, String val) {
        this.type = type;
        value = val;
    }
}
