package net.justgnat.mcfdoc.parser;

import java.util.LinkedList;
import java.util.List;

/**
 * A single datapack can contain any number of namespaced groups of functions
 * This represents a single one of those groups.
 */
public record FunctionGroup (String namespace, List<Function> functions) {

    public static class Builder {
        public String namespace;
        public List<Function> functions = new LinkedList<>();

        public FunctionGroup build() {
            return new FunctionGroup(namespace, functions);
        }
    }

}
