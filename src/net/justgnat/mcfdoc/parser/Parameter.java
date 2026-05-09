package net.justgnat.mcfdoc.parser;

/**
 * Represents a macro argument for a function.
 */
public record Parameter (String name, ParameterType type, String info) {

    @Override
    public String toString() {
        return "(Parameter Name=" + name + ", Type=" + type + ", Info=" + info + ")";
    }

}
