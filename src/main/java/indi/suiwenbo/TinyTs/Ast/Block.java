package indi.suiwenbo.TinyTs.Ast;

import java.util.ArrayList;
import java.util.List;

public class Block implements Node {

    protected List<Statement> stmts;

    public Block() {
        stmts = new ArrayList<>();
    }

    public void addStmt(Statement st) {
        stmts.add(st);
    }
}
