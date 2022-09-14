package indi.suiwenbo.TinyTs.Ast;

public class Var extends Statement {

    public final boolean isConst;

    public final String name;

    private ExprType type;

    public Var(String name, boolean isConst) {
        this.name = name;
        this.isConst = isConst;
    }

    public void setType(ExprType type) {
        this.type = type;
    }
}
