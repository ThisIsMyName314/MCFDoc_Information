package net.justgnat.mcfdoc.parser;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a single module in a datapack. Each module can contain a function directory
 */
public record FunctionGroup (String namespace, List<Function> functions) {

    public static class Builder {
        public String namespace;
        public List<Function> functions;

        public Builder() {
            reset();
        }

        /**
         * @return null if it is invalid or incomplete, otherwise a FunctionGroup
         */
        public FunctionGroup build() {
            if (namespace == null)
                return null;
            FunctionGroup module = new FunctionGroup(namespace, functions);
            reset();
            return module;
        }

        private void reset() {
            this.namespace = null;
            this.functions = new LinkedList<>();
        }
    }

}
