package net.justgnat.mcfdoc.sanitiser;

public record Tag(TagType type, boolean isClosing, String literal) {

    public static final Tag DISALLOWED = new Tag(TagType.DISALLOWED, false, null);

}
