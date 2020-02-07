package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isLetter;

public class Parser {
    public static List<Token> parse(String in) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < in.length(); i++) {
            if (isLetter(in.charAt(i))) {
                int length = getCurrentWordLength(in, i);
                tokens.add(new Token(Token.Type.VARIABLE, in.substring(i, i + length)));
                i += length - 1;
            } else if (in.charAt(i) == '\\') {
                i++;
                int length = getCurrentWordLength(in, i);
                tokens.add(new Token(Token.Type.LAMBDA, in.substring(i, i + length)));
                i += length - 1;
            } else if (in.charAt(i) == '(') {
                tokens.add(new Token(Token.Type.PARENS_OPEN));
            } else if (in.charAt(i) == ')') {
                tokens.add(new Token(Token.Type.PARENS_CLOSE));
            }
        }

        return tokens;
    }

    public static int getCurrentWordLength(String in, int pos) {
        for (int i = pos; i < in.length(); i++)
            if(!isLetter(in.charAt(i)))
                return i - pos;

        return in.length() - pos;
    }
}
