package indi.suiwenbo.TinyTs;

import indi.suiwenbo.TinyTs.Ast.Node;
import indi.suiwenbo.TinyTs.RawToken.RawIdent;
import indi.suiwenbo.TinyTs.RawToken.RawNumber;
import indi.suiwenbo.TinyTs.RawToken.RawToken;
import indi.suiwenbo.TinyTs.RawToken.ReservedOp;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private int pos = 0;
    private String plainText;
    private List<RawToken> rawTokenList;



    public Node parse(String text) {
        plainText = text;
        pos = 0;
        skipBlank();
        parseRawTokenList();
    }

    private void parseRawTokenList() {
        rawTokenList = new ArrayList<>();
        while (notEof()) {
            if (Character.isDigit(currentChar())) {
                rawTokenList.add(rawNumber());
            } else if (Character.isJavaIdentifierStart(currentChar())) {
                rawTokenList.add(rawIdText());
            } else {
                rawTokenList.add(switch (currentChar()) {
                    case '{', '}', '(', ')', '[', ']' -> {
                        pos++;
                        yield new ReservedOp("{");
                    }
                    case '+', '-' -> {
                        var cur = currentChar();
                        if ((pos + 1 < plainText.length())
                            && (plainText.charAt(pos + 1) == '='
                                || plainText.charAt(pos + 1) == cur)) {
                            int before = pos;
                            pos += 2;
                            yield new ReservedOp(plainText.substring(before, before + 2));
                        } else {
                            yield new ReservedOp(Character.toString(cur));
                        }
                    }
                    case '/', '%', '*' -> {
                        var cur = currentChar();
                        if (pos + 1 < plainText.length() && plainText.charAt(pos + 1) == '=') {
                            int before = pos;
                            pos += 2;
                            yield new ReservedOp(plainText.substring(before, before + 2));
                        } else {
                            yield new ReservedOp(Character.toString(cur));
                        }
                    }
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

    private RawNumber rawNumber() {
        int before = pos;
        while (notEof() && (Character.isDigit(currentChar()) || currentChar() == '.')) {
            pos++;
        }
        return new RawNumber(plainText.substring(before, pos));
    }


    private RawIdent rawIdText() {
        int before = pos;
        pos++;
        while (notEof() && Character.isJavaIdentifierPart(currentChar())) {
            pos++;
        }
        return new RawIdent(plainText.substring(before, pos));
    }



}
