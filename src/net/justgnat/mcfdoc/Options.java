package net.justgnat.mcfdoc;

import net.justgnat.mcfdoc.sanitiser.Strength;

public class Options {

    public String datapackPath;
    public String outputPath;
    public Strength html;
    public String author;
    public String description;
    public String version;
    public boolean canOverwrite;
    public boolean legacyNames;
    public boolean undefinedTags;
    public boolean prefixTypesInOutput;

    public Options() {
        datapackPath = null;
        outputPath = null;
        html = Strength.ALLOW_SAFE;
        author = "";
        description = "";
        version = "";
        canOverwrite = false;
        legacyNames = false;
        undefinedTags = false;
        prefixTypesInOutput = false;
    }

    /**
     * Attempts to accept the key/value
     * @throws RuntimeException if the flag is not recognised
     */
    public void accept(String key, String value) {
        switch (key) {
            case "dir": datapackPath = value;
                break;
            case "out": outputPath = value;
                break;
            case "html": html = Strength.parse(value);
                break;
            case "author": author = value;
                break;
            case "desc": description = value;
                break;
            case "overwrite": canOverwrite = value.equals("true");
                break;
            case "legacy": legacyNames = value.equals("true");
                break;
            case "undeftags": undefinedTags = value.equals("true");
                break;
            case "prefixtypes": prefixTypesInOutput = value.equals("true");
                break;
            case "version": version = value;
                break;
            default: throw new RuntimeException("Unknown flag '" + key + "'");
        }
    }

    /**
     * Validates the flags, ensuring the essential ones are present.
     * @throws RuntimeException if the flags are invalid
     */
    public void validate() {
        assertNotNull(datapackPath, "Missing datapack directory argument [dir=<absolute path>]");
        assertNotNull(outputPath, "Missing output directory argument [out=<directory path>]");
        // html Can be null if invalid value given
        assertNotNull(html, "Invalid html argument [html=<ALL|SAFE|NONE>]");
    }

    private void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new RuntimeException(message);
        }
    }

}
