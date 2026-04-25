package net.justgnat.mcfdoc.parser;

public record CustomType (String name, String description) {

    public static class Builder {
        public String name = "";
        public StringBuilder description = new StringBuilder(32);

        public CustomType build() {
            return new CustomType(name, description.toString());
        }
    }

}
