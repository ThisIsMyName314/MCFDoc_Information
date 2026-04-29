package net.justgnat.mcfdoc.parser;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser {

    private final List<String> lines;

    private final Function.Builder builder;

    private final boolean undefinedTags;

    private final TypeManager typeManager;

    private List<Tokeniser.Token> tokens;

    private int index;

    /**
     * Constructs a Function from the given lines.
     * @param functionName The full name of the function, with the namespace and full path.
     */
    public FunctionParser(String functionName, List<String> lines, boolean allowUndefinedTags, TypeManager typeManager) {
        this.lines = lines;
        builder = new Function.Builder();
        builder.name = functionName;
        this.undefinedTags = allowUndefinedTags;
        this.typeManager = typeManager;
    }

    private void error(String cause) {
        throw new RuntimeException(cause + " [" + builder.name + "]");
    }

    /**
     * Attempts to parse the given lines.
     * @throws RuntimeException if the comments in the MCFDoc are invalid.
     */
    public Function parse() {
        makeTokens();
        parseTokens();
        return builder.build();
    }

    private void makeTokens() {
        /* ArrayList for random access */
        tokens = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            if (!line.startsWith("#"))
                return;
            (new Tokeniser(line.substring(1), tokens)).parse();
        }
    }

    private void parseTokens() {
        for (index = 0; index < tokens.size(); ++index) {
            Tokeniser.Token token = current();
            switch (token.type) {
                case SPACE:
                    builder.description.append(' ');
                    break;
                case TAB:
                    builder.description.append('\t');
                    break;
                case STRING:
                    builder.description.append(token.literal);
                    break;
                case COLON:
                    builder.description.append(':');
                    break;
                case TAG:
                    parseTag(token);
                    break;
            }
        }
    }

    private void eatWhitespace() {
        do {
            ++index;
        } while (!atEnd() && currentIsWhitespace());
    }

    private boolean currentIsWhitespace() {
        Tokeniser.Type type = current().type;
        return type == Tokeniser.Type.SPACE || type == Tokeniser.Type.TAB;
    }

    private void parseTag(Tokeniser.Token tag) {
        switch (tag.literal) {
            case "author":
                builder.tags.add(new Tag.Author(getTagBody()));
                break;
            case "param":
                parseParam();
                break;
            case "returns":
            case "return":
                builder.tags.add(new Tag.Return(getTagBody()));
                break;
            case "see":
                builder.tags.add(new Tag.See(getTagBody()));
                break;
            case "version":
                builder.tags.add(new Tag.Version(getTagBody()));
                break;
            case "deprecated":
                builder.deprecated = true;
                builder.tags.add(new Tag.Deprecated(getTagBody()));
                break;
            case "since":
                builder.tags.add(new Tag.Since(getTagBody()));
                break;
            case "examples":
            case "example":
                builder.tags.add(new Tag.Example(getTagBody()));
                break;
            case "executor":
                builder.tags.add(new Tag.Executor(getTagBody()));
                break;
            case "hidden":
                builder.hidden = true;
                break;
            default: {
                parseUndefinedTag(tag.literal);
            }
        }
    }

    private void parseUndefinedTag(String tagString) {
        if (!undefinedTags) {
            error("Unknown tag '" + tagString + "'. Use the flag [undeftags=<true|false>] to decide whether to allow undefined tags");
        }
        Tag tag = Tag.from(tagString, getTagBody());
        if (tag != null) {
            builder.tags.add(tag);
        }
    }

    private Tokeniser.Token current() {
        if (atEnd())
            error("Unclosed or incomplete tag");
        return tokens.get(index);
    }

    private boolean atEnd() {
        return tokens.size() == index;
    }

    private void parseParam() {
        eatWhitespace();
        String name = getParameterName();
        eatWhitespace();
        /* The colon before the type is optional */
        if (current().type == Tokeniser.Type.COLON) {
            eatWhitespace();
        }
        Parameter.ParamType type = getParameterType();
        eatWhitespace();
        String description = greedyString();
        builder.parameters.add(new Parameter(name, type, description));
    }

    private String getParameterName() {
        Tokeniser.Token current = current();
        switch (current.type) {
            case STRING: return current.literal;
            case TYPE_BOOL: return "Bool";
            case TYPE_FLOAT: return "Float";
            case TYPE_OTHER: return "Other";
            case TYPE_INT: return "Int";
            case TYPE_STRING: return "String";
            default: error("Expected name for parameter");
        }
        return null;
    }

    private Parameter.ParamType getParameterType() {
        return switch (current().type) {
            case TYPE_BOOL -> new Parameter.ParamType("Bool");
            case TYPE_FLOAT -> new Parameter.ParamType("Float");
            case TYPE_STRING -> new Parameter.ParamType("String");
            case TYPE_INT -> new Parameter.ParamType("Int");
            case TYPE_OTHER -> new Parameter.ParamType("Other");
            default -> {
                if (current().type == Tokeniser.Type.STRING) {
                    String name = current().literal;
                    typeManager.checkTypeIsDefined(name);
                    yield new Parameter.ParamType(name);
                }
                error("Invalid type for parameter. Use 'Other' to indicate other types");
                yield null;
            }
        };
    }

    private String getTagBody() {
        ++index;
        if (atEnd()) throw new RuntimeException("Missing body of tag");
        return greedyString();
    }

    /**
     * Eats tokens until a new tag, and returns them as a string.
     */
    private String greedyString() {
        StringBuilder sb = new StringBuilder();

        while (!atEnd()) {
            Tokeniser.Token token = current();
            /* Stops when a new tag is met */
            if (token.type == Tokeniser.Type.TAG) {
                --index;
                return sb.toString();
            }
            switch (token.type) {
                case SPACE: sb.append(' '); break;
                case TAB: sb.append('\t'); break;
                case TYPE_BOOL: sb.append("Bool"); break;
                case TYPE_INT: sb.append("Int"); break;
                case TYPE_FLOAT: sb.append("Float"); break;
                case TYPE_OTHER: sb.append("Other"); break;
                case TYPE_STRING: sb.append("String"); break;
                case STRING: sb.append(token.literal); break;
                case COLON: sb.append(":"); break;
            }
            ++index;
        }
        return sb.toString();
    }
}
