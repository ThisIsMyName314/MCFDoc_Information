package net.justgnat.mcfdoc.parser;

import net.justgnat.mcfdoc.sanitiser.HtmlSanitiser;
import net.justgnat.mcfdoc.sanitiser.Strength;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Datapack with metadata.
 * @param name the name of the datapack, which has already been sanitised.
 */
public record Datapack (
        String name,
        String author,
        String description,
        String version,
        List<FunctionGroup> groups) {

    public boolean hasDescription() {
        return !description.isEmpty();
    }

    public boolean hasAuthors() {
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
            name = HtmlSanitiser.sanitise(name, Strength.NONE);
            return new Datapack(name, author, description, version, groups);
        }

    }

}
