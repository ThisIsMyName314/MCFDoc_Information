package net.justgnat.mcfdoc.parser;

import java.util.LinkedList;
import java.util.List;

public record Function (
        String name, /* The name includes the path and namespace, for example 'datapack:util/function' */
        String description,
        List<Parameter> parameters,
        TagGroup tags,
        boolean isDeprecated) {

    @Override
    public String toString() {
        return "Function (Name="+name+", Desc="+description+", Params="+parameters+", Tags="+tags+")";
    }

    public static class Builder {
        public String name;
        public StringBuilder description;
        public List<Parameter> parameters;
        public TagGroup tags;
        public boolean deprecated;

        public Builder() {
            parameters = new LinkedList<>();
            tags = new TagGroup();
            description = new StringBuilder();
            deprecated = false;
        }

        public Function build() {
            return new Function(name, description.toString(), parameters, tags, deprecated);
        }
    }

}
