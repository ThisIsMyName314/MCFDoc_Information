package net.justgnat.mcfdoc.parser;

import net.justgnat.mcfdoc.sanitiser.HtmlSanitiser;
import net.justgnat.mcfdoc.sanitiser.Strength;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a function file
 * @param name the full name of the function, already sanitised.
 * @param simpleName does not need sanitisation
 */
public record Function (
        String name,
        String simpleName,
        String description,
        List<Parameter> parameters,
        Annotations annotations,
        boolean isDeprecated,
        boolean isHidden) {

    @Override
    public String toString() {
        return "Function (Name=" + name + ", Desc=" + description + ", Params=" + parameters + ", Annotations=" + annotations + ")";
    }

    public static class Builder {
        public String name;
        public String simpleName;
        public StringBuilder description;
        public List<Parameter> parameters;
        public Annotations annotations;
        public boolean deprecated;
        public boolean hidden;

        public Builder() {
            parameters = new LinkedList<>();
            annotations = new Annotations();
            description = new StringBuilder();
            deprecated = false;
            hidden = false;
        }

        public Function build() {
            name = HtmlSanitiser.sanitise(name, Strength.NONE);
            return new Function(name, simpleName, description.toString(), parameters, annotations, deprecated, hidden);
        }
    }

}
