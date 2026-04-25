package net.justgnat.mcfdoc.sanitiser;

public enum Strength {

    /*
    No HTML elements can be used
     */
    TOTAL,

    /*
    A small selection of safe HTML elements are allowed (see TagType)
     */
    ALLOW_SAFE,

    /*
    No sanitation will occur
     */
    NONE;

    public static Strength parse(String string) {
        return switch (string) {
            case "NONE" -> TOTAL;
            case "SAFE" -> ALLOW_SAFE;
            case "ALL" -> NONE;
            default -> null;
        };
    }

}
