package net.justgnat.mcfdoc.parser;

import java.util.LinkedList;
import java.util.List;

public record Datapack (String name, String author, String description, String version, List<FunctionGroup> groups) {

    public boolean hasDescription() {
        return !description.isEmpty();
    }

    public boolean hasCredits() {
        return !author.isEmpty();
    }

    public boolean hasVersion() {
        return !version.isEmpty();
    }

    public static class Builder {

        public String name;
        public String author;
        public String description;
        public String version;
        public List<FunctionGroup> groups = new LinkedList<>();

        public Datapack build() {
            return new Datapack(name, author, description, version, groups);
        }

    }

}
