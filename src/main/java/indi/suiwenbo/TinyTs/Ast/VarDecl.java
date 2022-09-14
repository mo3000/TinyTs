package indi.suiwenbo.TinyTs.Ast;

import java.util.List;
import java.util.Optional;

public class VarDecl extends Statement {
    private List<Var> vars;

    private Optional<Expr> initVal;

    public List<Var> getVars() {
        return vars;
    }

    public Expr getInitVal() {
        return initVal.get();
    }

    public boolean hasInitValue() {
        return initVal.isPresent();
    }

    public void setVars(List<Var> vars) {
        this.vars = vars;
    }

    public void setInitVal(Expr expr) {
        initVal = Optional.of(expr);
    }
}
