package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.Iterator;
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

    public static Token makeTree(List<Token> in) {
        Iterator<Token> iterator = in.iterator();
        return makeTree(in, iterator);
    }

    public static Token makeTree(List<Token> in, Iterator<Token> iterator) {
        Token current = null;
        while (iterator.hasNext()) {
            Token token = iterator.next().copy();
            if (token.type == Token.Type.PARENS_CLOSE) break;
            if (token.type == Token.Type.PARENS_OPEN)
                token = makeTree(in, iterator);

            if (current == null) current = token;
            else if (current.getNumChildren() == current.type.getMaxChildren()) {
                Token app = new Token(Token.Type.APPLICATION);
                app.addChild(current);
                app.addChild(token);

                if (current.getParent() != null) {
                    current.getParent().clearChildren();
                    current.getParent().addChild(app);
                    app.setParent(current);
                }
                current = app;
            }
            else {
                current.addChild(token);
                token.setParent(current);
                current = token;
            }
        }

        assert current != null;
        while (current.getParent() != null)
            current = current.getParent();
        return current;
    }

    public static int getCurrentWordLength(String in, int pos) {
        for (int i = pos; i < in.length(); i++)
            if(!isLetter(in.charAt(i)))
                return i - pos;

        return in.length() - pos;
    }
}
