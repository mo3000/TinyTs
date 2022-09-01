package indi.suiwenbo.TinyTs.Ast;

public enum Operator {
    Plus("+"), Minus("-");

    private String name;
    Operator(String name) {
        this.name = name;
    }
}
