package net.justgnat.mcfdoc.parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class TypeManager {

    public static class UndefinedTypeException extends RuntimeException {
        public UndefinedTypeException(String cause) {
            super(cause);
        }
    }

    private final HashSet<String> types;

    private final List<CustomType> customTypes;

    public TypeManager() {
        types = new HashSet<>(8);
        customTypes = new LinkedList<>();
        addDefaultTypes();
    }

    private void addDefaultTypes() {
        types.add("Int");
        types.add("Float");
        types.add("String");
        types.add("Bool");
        types.add("Short");
        types.add("Long");
        types.add("Array");
    }

    public boolean define(CustomType newType) {
        if (!types.add(newType.name())) {
            return false;
        }
        customTypes.add(newType);
        return true;
    }

    public List<CustomType> getCustomTypes() {
        return this.customTypes;
    }

    /**
     * @throws UndefinedTypeException if the type has not been defined
     */
    public void checkTypeIsDefined(String type) throws UndefinedTypeException {
        if (!types.contains(type)) {
            checkArrayType(type);
        }
    }

    /*
    Checks that the type, that must be an array, is valid.
     */
    private void checkArrayType(String type) throws UndefinedTypeException {
        int firstBracket = type.indexOf('[');
        if (firstBracket == -1) {
            throw new UndefinedTypeException("Undefined type '" + type + "'");
        }

        String typeName = type.substring(0, firstBracket);
        if (!types.contains(typeName)) {
            throw new UndefinedTypeException("Undefined type '" + typeName + "'");
        }

        validateArrayBrackets(type, firstBracket);
    }

    private void validateArrayBrackets(String type, int firstBracket) throws UndefinedTypeException {
        boolean closed = false;
        for (int i = firstBracket + 1; i < type.length(); i++) {
            char chr = type.charAt(i);
            if (closed) {
                if (chr == '[') closed = false;
            } else {
                if (chr == ']') closed = true;
                else throw new UndefinedTypeException("Missing closing bracket for array type '" + type + "'");
            }
        }
        if (!closed)
            throw new UndefinedTypeException("Missing closing bracket for array type '" + type + "'");
    }

}
