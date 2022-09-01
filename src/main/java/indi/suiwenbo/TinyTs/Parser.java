package indi.suiwenbo.TinyTs;

import indi.suiwenbo.TinyTs.Ast.Node;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private int pos = 0;
    private String plainText;
    private List<String> rawTokenList;

    public Node parse(String text) {
        plainText = text;
        pos = 0;
        skipBlank();
        rawTokenList = new ArrayList<>();
        while (!eof()) {
            if (Character.isDigit(currentChar())) {
                rawTokenList.add(rawNumber());
            } else if (Character.isAlphabetic(currentChar())) {

            }
            skipBlank();
        }
    }

    private void skipBlank() {
        while (!eof() && Character.isSpaceChar(plainText.charAt(pos))) {
            pos++;
        }
    }

    private char currentChar() {
        return plainText.charAt(pos);
    }

    private boolean eof() {
        return pos < plainText.length();
    }

    private String rawNumber() {
        int before = pos;
        while ((!eof()) && (Character.isDigit(currentChar()) || currentChar() == '.')) {
            pos++;
        }
        return plainText.substring(before, pos);
    }



}
