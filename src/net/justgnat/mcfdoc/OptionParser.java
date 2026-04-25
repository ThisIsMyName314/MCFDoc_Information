package net.justgnat.mcfdoc;

public class OptionParser {

    private final String[] flags;

    public OptionParser(String[] flags) {
        this.flags = flags;
    }

    /**
     * Parses the String arguments and constructs an Args Object.
     * @throws RuntimeException for any unknown, invalid or missing args.
     */
    public static Options parse(String[] flags) {
        OptionParser parser = new OptionParser(flags);
        return parser.parse();
    }

    private Options parse() {
        Options args = new Options();

        for (String flag : flags) {
            parseArg(flag, args);
        }

        args.validate();
        return args;
    }

    private void parseArg(String flag, Options args) {
        int splitPoint = flag.indexOf('='); // As long as no keys have an = sign then this is fine.
        if (splitPoint == -1) {
            throw new RuntimeException("Invalid argument '" + flag + "', missing value");
        }

        parseArg(flag.substring(0, splitPoint), flag.substring(splitPoint + 1), args);
    }

    private void parseArg(String key, String value, Options args) {
        value = validateArg(key, value);
        args.accept(key, value);
    }

    private String validateArg(String key, String value) {
        if (value.isEmpty())
            throw new RuntimeException("Empty argument for flag " + key);

        // Trim quotes from quoted values
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
