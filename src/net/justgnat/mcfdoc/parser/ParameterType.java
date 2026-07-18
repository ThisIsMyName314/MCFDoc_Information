package net.justgnat.mcfdoc.parser;

public class ParameterType {

    public static final ParameterType
        INT = new ParameterType("Int"),
        BOOL = new ParameterType("Bool"),
        FLOAT = new ParameterType("Float"),
        STRING = new ParameterType("String"),
        OTHER = new ParameterType("Other");

    private final String colouredName;

    /**
     * A Type for parameters. Custom types are constructed with the
     * static custom(String) method.
     */
    private ParameterType(String name) {
        this.colouredName = "<span class=\"type\">" + name + "</span>";
    }

    public static ParameterType custom(String name) {
        return new ParameterType(name);
    }

    public String getColouredName() {
        return this.colouredName;
    }

}
