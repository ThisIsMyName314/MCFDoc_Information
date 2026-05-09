package net.justgnat.mcfdoc.parser;

import net.justgnat.mcfdoc.helper.StringHelper;

public abstract class Annotation {

    private final String tagName, body;

    /**
     * Represents an @annotation from a comment
     * @param tagName the name after the @ symbol
     * @param body the content provided after the annotation
     */
    public Annotation(String tagName, String body) {
        this.tagName = tagName;
        this.body = body;
    }

    /**
     * Constructs a custom annotation. Not to be used for inbuilt ones.
     * @return a Plain annotation
     */
    public static Annotation from(String tagName, String body) {
        if (tagName.isEmpty())
            return null;

        tagName = StringHelper.capitaliseFirstCharacter(tagName);

        return new Plain(tagName, body);
    }

    public String getName() {
        return tagName;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "[" + tagName + "=" + body + "]";
    }

    public static class Return extends Annotation {
        public Return(String body) {
            super("Returns", body);
        }
    }

    public static class Author extends Annotation {
        public Author(String body) {
            super("Authors", body);
        }
    }

    public static class Deprecated extends Annotation {
        public Deprecated(String body) {
            super("Deprecated", body);
        }
    }

    public static class Version extends Annotation {
        public Version(String body) {
            super("Version", body);
        }
    }

    public static class See extends Annotation {
        public See(String body) {
            super("See", body);
        }
    }

    public static class Since extends Annotation {
        public Since(String body) {
            super("Since", body);
        }
    }

    public static class Example extends Annotation {
        public Example(String body) {
            super("Example", body);
        }
    }

    public static class Executor extends Annotation {
        public Executor(String body) {
            super("Executor", body);
        }
    }

    private static class Plain extends Annotation {
        public Plain(String name, String body) {
            super(name, body);
        }
    }

}
