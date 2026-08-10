package net.justgnat.mcfdoc;

public class Main {

    public static final String VERSION = "v2.1.0";

    public static void main(String[] args) {
        if (args.length == 0 || args[0].endsWith("help")) {
            Logger.write(
                "MCFDOC " + VERSION +
                """
                \n
                Required
                dir=<datapack path>         the path to the datapack
                out=<output path>           the path to the output file

                Optional
                html=<ALL|SAFE|NONE>        determines which html tags can be used in datapack comments
                desc=<description>          description of the datapack
                version=<version>           version of the datapack
                author=<author>             author of the datapack
                overwrite=<true|false>      whether to overwrite the output file
                prefixtypes=<true|false>    whether to write macro types as prefixes or suffixes
                undeftags=<true|false>      whether to allow undefined tags
                """
            );
        } else {
            Executor executor = new Executor(args);
            executor.execute();
        }
    }

}
