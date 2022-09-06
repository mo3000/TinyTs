package indi.suiwenbo.TinyTs;

import indi.suiwenbo.TinyTs.Ast.Block;
import indi.suiwenbo.TinyTs.Ast.Node;
import indi.suiwenbo.TinyTs.Ast.Statement;
import indi.suiwenbo.TinyTs.RawToken.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private int pos = 0;
    private String plainText;
    private List<RawToken> rawTokenList;

    private final Set<String> keyword;

    private List<Node> ast;


    public Parser() {
        keyword = new HashSet<>();
        for (var e : Keyword.values()) {
            keyword.add(e.toString());
        }
    }



    public void parse(String text) {
        plainText = text;
        pos = 0;
        skipBlank();
        parseRawTokenList();
        buildAst();
    }

    private void parseRawTokenList() {
        rawTokenList = new ArrayList<>();
        while (notEof()) {
            if (Character.isDigit(currentChar())) {
                rawTokenList.add(rawNumber());
            } else if (Character.isJavaIdentifierStart(currentChar())) {
                var text = rawPossibleIdent();
                if (keyword.contains(text)) {
                    rawTokenList.add(new ReservedWord(text));
                } else {
                    rawTokenList.add(new RawIdent(text));
                }
            } else {
                rawTokenList.add(switch (currentChar()) {
                    case '{', '}', '(', ')', '[', ']' -> {
                        pos++;
                        yield new ReservedOp("{");
                    }
                    case ';' -> new ReservedOp(";");
                    case '+' -> probeOp2('+', '=');
                    case '-' -> probeOp2('-', '=');
                    case '/', '%', '*', '!' -> probeOp2('=');
                    case '=' -> probeOp2('>', '=');
                    case '<' -> probeOp2('=', '<');
                    case '>' -> probeOp2('=', '>');
                    default -> throw new RuntimeException("error " + currentChar());
                });
            }
            skipBlank();
        }
    }

    private void skipBlank() {
        while (notEof() && Character.isSpaceChar(plainText.charAt(pos))) {
            pos++;
        }
    }

    private char currentChar() {
        return plainText.charAt(pos);
    }

    private boolean notEof() {
        return pos >= plainText.length();
    }

    private boolean nextCharEq(char... chars) {
        if (pos + 1 < plainText.length()) {
            char next = plainText.charAt(pos + 1);
            for (char c : chars) {
                if (c == next) {
                    return true;
                }
            }
        }
        return false;
    }

    private ReservedOp probeOp2(char... chars) {
        var cur = currentChar();
        if (nextCharEq(chars)) {
            int before = pos;
            pos += 2;
            return new ReservedOp(plainText.substring(before, before + 2));
        } else {
            advance();
            return new ReservedOp(Character.toString(cur));
        }
    }


    private RawNumber rawNumber() {
        int before = pos;
        while (notEof() && (Character.isDigit(currentChar()) || currentChar() == '.')) {
            pos++;
        }
        return new RawNumber(plainText.substring(before, pos));
    }


    private String rawPossibleIdent() {
        int before = pos;
        advance();
        while (notEof() && Character.isJavaIdentifierPart(currentChar())) {
            advance();
        }
        return plainText.substring(before, pos);
    }

    private void advance() {
        pos++;
    }

    private void buildAst() {
        pos = 0;
        ast = new ArrayList<>();
        while (pos < rawTokenList.size()) {
            block();
            advance();
        }
    }


    private RawToken currentToken() {
        return rawTokenList.get(pos);
    }



    private Block block() {
        boolean hasBlockStart = tokenEquals("{");
        if (hasBlockStart) {
            advance();
        }
        var blk = blockBody(!hasBlockStart);
        if (hasBlockStart) {
            assert(tokenEquals("}"));
            advance();
        }
        return blk;
    }

    private Block blockBody(boolean oneSentenceBody) {
        var blk = new Block();
        if (oneSentenceBody) {
            blk.addStmt(statement());
            advance();
        } else {
            while (!tokenEquals("}")) {
                blk.addStmt(statement());
            }
        }
        return blk;
    }


    private boolean tokenEquals(RawToken obj1, RawToken obj2) {
        return obj1.name().equals(obj2.name());
    }

    private boolean tokenEquals(RawToken obj1, String obj2) {
        return obj1.name().equals(obj2);
    }

    private boolean tokenEquals(String obj2) {
        return currentToken().name().equals(obj2);
    }

    private Statement statement() {

    }

    private void expr() {

    }
}
