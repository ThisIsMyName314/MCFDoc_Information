package net.justgnat.mcfdoc.parser;

/*
A parameter is a macro that is required for the function to run
 */
public record Parameter (String name, ParamType type, String info) {

    public record ParamType(String name) {
        public String getColouredName() {
            return "<font color=\"#AA00AA\">" + this.name + "</font>";
        }
    }

    @Override
    public String toString() {
        return "(Parameter Name=" + name + ", Type=" + type + ", Info=" + info + ")";
    }

}
