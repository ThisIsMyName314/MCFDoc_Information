package net.justgnat.mcfdoc.parser;

public abstract class Tag {

    private final String tagName, body;

    public Tag(String tagName, String body) {
        this.tagName = tagName;
        this.body = body;
    }

    public static Tag from(String title, String body) {
        if (title.isEmpty())
            return null;

        // Capitalise the first letter for the title
        char firstChar = title.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            firstChar = (char)(firstChar - 32);
            title = firstChar + title.substring(1);
        }

        return new Plain(title, body);
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

    public static class Return extends Tag {
        public Return(String body) {
            super("Returns", body);
        }
    }

    public static class Author extends Tag {
        public Author(String body) {
            super("Authors", body);
        }
    }

    public static class Deprecated extends Tag {
        public Deprecated(String body) {
            super("Deprecated", body);
        }
    }

    public static class Version extends Tag  {
        public Version(String body) {
            super("Version", body);
        }
    }

    public static class See extends Tag {
        public See(String body) {
            super("See", body);
        }
    }

    public static class Since extends Tag {
        public Since(String body) {
            super("Since", body);
        }
    }

    public static class Example extends Tag {
        public Example(String body) {
            super("Example", body);
        }
    }

    public static class Executor extends Tag {
        public Executor(String body) {
            super("Executor", body);
        }
    }

    private static class Plain extends Tag {
        public Plain(String name, String body) {
            super(name, body);
        }
    }

}
