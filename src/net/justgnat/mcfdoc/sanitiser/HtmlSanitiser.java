package net.justgnat.mcfdoc.sanitiser;

public class HtmlSanitiser {

    private final String input;

    private final Strength strength;

    private StringBuilder sb;

    private int index;

    public HtmlSanitiser(String input, Strength strength) {
        this.input = input;
        this.strength = strength;
    }

    /**
     * Sanitises the input and returns a modified version.
     */
    public String sanitise() {
        if (strength == Strength.NONE) {
            return input;
        }

        // Assume we need roughly 10% more space
        sb = new StringBuilder((int)(input.length() * 1.1));

        index = 0;

        for (; index < input.length(); ++index) {
            char chr = input.charAt(index);
            switch (chr) {
                case '&': emitAmpersand(); break;
                case '"': emitDoubleQuote(); break;
                case '\'': emitSingleQuote(); break;
                case '<': emitLesserThan(); break;
                case '>': emitGreaterThan(); break;
                default: sb.append(chr);
            }
        }

        return sb.toString();
    }

    private void emitAmpersand() {
        sb.append("&amp;");
    }

    private void emitDoubleQuote() {
        sb.append("&quot;");
    }

    private void emitSingleQuote() {
        sb.append("&#039;");
    }

    private void emitGreaterThan() {
        sb.append("&gt;");
    }

    private void emitLesserThan() {
        if (strength == Strength.ALLOW_SAFE) {
            ++index; // Move past the <
            Tag tag = readTag();
            if (tag.type() != TagType.DISALLOWED) {
                sb.append('<');
                sb.append(tag.literal());
                sb.append('>');
                index += tag.literal().length();
            } else {
                // Move back to the <
                --index;
                sb.append("&lt;");
            }
        } else {
            sb.append("&lt;");
        }
    }

    private Tag readTag() {
        int i = index; // Works on a copy of the index
        StringBuilder builder = new StringBuilder();
        outerHere:
        for (; i < input.length(); ++i) {
            char chr = input.charAt(i);
            switch (chr) {
                case '<': // We do not allow any nested structures
                    return Tag.DISALLOWED;
                case '>':
                    break outerHere;
                default:
                    builder.append(chr);
            }
        }

        if (builder.isEmpty())
            return Tag.DISALLOWED;

        String tagString = builder.toString();

        boolean isClosing = tagString.charAt(0) == '/';

        TagType type = isClosing ?
                parseTagType(tagString.substring(1)) :
                parseTagType(tagString);

        return new Tag(type, isClosing, tagString);
    }

    private TagType parseTagType(String string) {
        return switch (string) {
            case "br" -> TagType.BR;
            case "strong" -> TagType.STRONG;
            case "i" -> TagType.I;
            case "small" -> TagType.SMALL;
            case "big" -> TagType.BIG;
            case "ins" -> TagType.INS;
            case "del" -> TagType.DEL;
            default -> TagType.DISALLOWED;
        };
    }

}
