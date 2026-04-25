package net.justgnat.mcfdoc.parser;

import net.justgnat.mcfdoc.Logger;

import java.util.List;

public class TypeParser {

    public static class InvalidTypeNameException extends RuntimeException {
        public InvalidTypeNameException(String cause) {
            super(cause);
        }
    }

    private final List<String> lines;

    private final TypeManager manager;

    private CustomType.Builder builder;

    public TypeParser(List<String> lines, TypeManager typeManager) {
        this.lines = lines;
        this.manager = typeManager;
    }

    public void parse() throws InvalidTypeNameException {
        /*
        Format
        #typename
            description
        #typename2
            description
         */
        builder = new CustomType.Builder();
        for (String line : lines) {
            if (!line.isEmpty() && line.charAt(0) != '!') // ! is a comment prefix
                parseLine(line);
        }

        // Add the last defined type if there was one
        if (!builder.name.isEmpty()) {
            buildAndDefineType();
        }
    }

    private void buildAndDefineType() {
        CustomType type = builder.build();
        if (manager.define(type)) {
            Logger.write("Defined new type '" + type.name() + "'");
        } else {
            Logger.write("Can not define type '" + type.name() + "' because it already exists");
        }
    }

    private void parseLine(String line) {
        if (line.charAt(0) == '#') {
            // If something has already been defined, add it
            if (!builder.name.isEmpty()) {
                buildAndDefineType();
                builder = new CustomType.Builder();
            }

            String typeName = line.substring(1);

            if (!isValidTypeName(typeName)) {
                throw new InvalidTypeNameException("Invalid type name '" + typeName + "'");
            }

            builder.name = typeName;
        } else {
            builder.description.append(line);
        }
    }

    private boolean isValidTypeName(String typeName) {
        if (typeName.isEmpty())
            return false;

        for (int i = 0; i < typeName.length(); i++) {
            char chr = typeName.charAt(i);
            if (!isValidChar(chr))
                return false;
        }
        return true;
    }

    private boolean isValidChar(char chr) {
        return (chr >= 'a' && chr <= 'z')
                || (chr >= 'A' && chr <= 'Z')
                || (chr >= '0' && chr <= '9')
                || chr == '_';
    }

}
