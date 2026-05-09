package net.justgnat.mcfdoc.parser;

/**
 * Represents a Type defined by the user in the types file.
 */
public record CustomType (String name, String description) {

    public static class Builder {
        public String name = "";
        public StringBuilder description = new StringBuilder();

        public CustomType build() {
            return new CustomType(name, description.toString());
        }
    }

}