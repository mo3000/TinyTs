package indi.suiwenbo.TinyTs.RawToken;

public enum Keyword {
    CLASS("class"), STATIC("static"), IMPORT("import"),
    EXPORT("export"), FUNCTION("function"), LET("let"),
    CONST("const"),
    ;

    private final String name;

    Keyword(String name) {
        this.name = name;
    };

    @Override
    public String toString() {
        return name;
    }
}
