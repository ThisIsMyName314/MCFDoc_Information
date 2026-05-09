package net.justgnat.mcfdoc.parser;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser {

    private static final String COMMENT_PREFIX = "#";

    private final List<String> lines;

    private final Function.Builder builder;

    private final boolean undefinedTags;

    private final TypeManager typeManager;

    private List<Tokeniser.Token> tokens;

    private int index;

    public FunctionParser(String fullName, String simpleName, List<String> lines, boolean allowUndefinedTags, TypeManager typeManager) {
        this.lines = lines;
        builder = new Function.Builder();
        builder.name = fullName;
        builder.simpleName = simpleName;
        this.undefinedTags = allowUndefinedTags;
        this.typeManager = typeManager;
    }

    private void error(String cause) {
        throw new RuntimeException(cause + " [" + builder.name + "]");
    }

    /**
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

            if (!line.startsWith(COMMENT_PREFIX))
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
                    parseAnnotation(token);
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

    private void parseAnnotation(Tokeniser.Token tag) {
        switch (tag.literal) {
            case "author":
                builder.annotations.add(new Annotation.Author(getAnnotationBody()));
                break;
            case "param":
                parseParam();
                break;
            case "returns":
            case "return":
                builder.annotations.add(new Annotation.Return(getAnnotationBody()));
                break;
            case "see":
                builder.annotations.add(new Annotation.See(getAnnotationBody()));
                break;
            case "version":
                builder.annotations.add(new Annotation.Version(getAnnotationBody()));
                break;
            case "deprecated":
                builder.deprecated = true;
                builder.annotations.add(new Annotation.Deprecated(getAnnotationBody()));
                break;
            case "since":
                builder.annotations.add(new Annotation.Since(getAnnotationBody()));
                break;
            case "examples":
            case "example":
                builder.annotations.add(new Annotation.Example(getAnnotationBody()));
                break;
            case "executor":
                builder.annotations.add(new Annotation.Executor(getAnnotationBody()));
                break;
            case "hidden":
                builder.hidden = true;
                break;
            default: {
                parseUndefinedAnnotation(tag.literal);
            }
        }
    }

    private void parseUndefinedAnnotation(String name) {
        if (!undefinedTags) {
            error("Unknown annotation '" + name + "'. Use the flag [undeftags=<true|false>] to decide whether to allow undefined annotations");
        }
        Annotation annotation = Annotation.from(name, getAnnotationBody());
        if (annotation != null) {
            builder.annotations.add(annotation);
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
        ParameterType type = getParameterType();
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
        return null; // Unreachable
    }

    private ParameterType getParameterType() {
        return switch (current().type) {
            case TYPE_BOOL -> ParameterType.BOOL;
            case TYPE_FLOAT -> ParameterType.FLOAT;
            case TYPE_STRING -> ParameterType.STRING;
            case TYPE_INT -> ParameterType.INT;
            case TYPE_OTHER -> ParameterType.OTHER;
            default -> {
                String name = current().literal;
                if (current().type == Tokeniser.Type.STRING) {
                    typeManager.checkTypeIsDefined(name);
                    yield ParameterType.custom(name);
                }
                error("Undefined type '" + name + "'. Define custom types in a types file.");
                yield null;
            }
        };
    }

    private String getAnnotationBody() {
        ++index;
        if (atEnd()) throw new RuntimeException("Missing body of tag");
        return greedyString();
    }

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
