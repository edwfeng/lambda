package org.bergen.atcs.atics.lambda;

import static java.lang.Character.isLetter;

public class Parser {
    public static int getCurrentWordLength(String in, int pos) {
        if (!isLetter(in.charAt(pos)))
            return -1;

        for (int i = pos; i < in.length(); i++)
            if(!isLetter(in.charAt(i)))
                return i - pos;

        return in.length() - pos;
    }
}
