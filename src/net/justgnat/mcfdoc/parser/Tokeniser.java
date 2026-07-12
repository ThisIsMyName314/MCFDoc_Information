package net.justgnat.mcfdoc.parser;

import java.util.List;

public class Tokeniser {

    private static final char ANNOTATION_PREFIX = '@';

    public enum Type {
        STRING, TAG, COLON, TYPE_INT, TYPE_FLOAT, TYPE_STRING, TYPE_BOOL, TYPE_OTHER, SPACE, TAB;
    }

    public static class Token {
        public String literal;
        public final Type type;

        public Token(Type type, String literal) {
            this.literal = literal;
            this.type = type;
        }

        public String toString() {
            return "(" + type + (literal == null ? ")" : "=" + literal + ")");
        }
    }

    private String line;

    private int index;

    private final List<Token> tokens;

    public Tokeniser(String line, List<Token> tokens) {
        this.line = line;
        this.tokens = tokens;
    }

    public void parse() {
        line = line.trim();

        if (line.isEmpty())
            return;

        if (line.charAt(0) != ANNOTATION_PREFIX) {
            addString(line, tokens);
            return;
        }

        index = 1;

        // Get the tag after the ANNOTATION_PREFIX
        StringBuilder sb = new StringBuilder();
        for (; index < line.length(); ++index) {
            char chr = line.charAt(index);
            if (chr == ' ' || chr == '\n' || chr == '\t' || chr == ':') {
                break;
            }
            sb.append(chr);
        }

        // Add the tag as a token
        tokens.add(new Token(Type.TAG, sb.toString()));

        // Read the tag body
        for (;index < line.length(); ++index) {
            char chr = line.charAt(index);
            switch (chr) {
                case ' ':
                    tokens.add(token(Type.SPACE));
                    break;
                case '\t':
                    tokens.add(token(Type.TAB));
                    break;
                case ':':
                    tokens.add(token(Type.COLON));
                    break;
                default: parseWord(line);
            }
        }
    }

    private void parseWord(String line) {
        StringBuilder sb = new StringBuilder();

        // read until a break such as a space
        for (; index < line.length(); ++index) {
            char chr = line.charAt(index);
            if (chr == ' ' || chr == '\n' || chr == '\t' || chr == ':') {
                break;
            }
            sb.append(chr);
        }

        --index;

        switch (sb.toString()) {
            case "Int": tokens.add(token(Type.TYPE_INT)); break;
            case "String": tokens.add(token(Type.TYPE_STRING)); break;
            case "Float": tokens.add(token(Type.TYPE_FLOAT)); break;
            case "Bool": tokens.add(token(Type.TYPE_BOOL)); break;
            case "Other": tokens.add(token(Type.TYPE_OTHER)); break;
            default: addString(sb.toString(), tokens);
        }
    }

    /**
     * Adds a string. If the last token was also a string, it merges the two with a linebreak inbetween
     */
    private void addString(String string, List<Token> tokens) {
        if (!tokens.isEmpty()) {
            if (tokens.getLast().type == Type.STRING) {
                tokens.getLast().literal += "<br>" + string;
                return;
            }
        }
        tokens.add(new Token(Type.STRING, string));
    }

    private Token token(Type type) {
        return new Token(type, null);
    }

}
