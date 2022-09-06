package indi.suiwenbo.TinyTs;

import indi.suiwenbo.TinyTs.Ast.Node;
import indi.suiwenbo.TinyTs.RawToken.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private int pos = 0;
    private String plainText;
    private List<RawToken> rawTokenList;

    private Set<String> keyword;


    public Parser() {
        keyword = new HashSet<>();
        for (var e : Keyword.values()) {
            keyword.add(e.toString());
        }
    }



    public Node parse(String text) {
        plainText = text;
        pos = 0;
        skipBlank();
        parseRawTokenList();
        return buildAst();
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

    private RawToken probeOp2(char... chars) {
        var cur = currentChar();
        if (nextCharEq(chars)) {
            int before = pos;
            pos += 2;
            return new ReservedOp(plainText.substring(before, before + 2));
        } else {
            pos++;
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
        pos++;
        while (notEof() && Character.isJavaIdentifierPart(currentChar())) {
            pos++;
        }
        return plainText.substring(before, pos);
    }

    private Node buildAst() {
        pos = 0;
        while (pos < rawTokenList.size()) {

        }
    }
}
