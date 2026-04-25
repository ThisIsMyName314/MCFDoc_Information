package net.justgnat.mcfdoc;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].endsWith("help")) {
            System.out.println(
                """
                MCFDoc v1.0.0
                
                Required
                dir=<datapack path>
                out=<output path>

                Optional
                html=<ALL|SAFE|NONE>
                desc=<description>
                version=<version>
                author=<author>
                overwrite=<true|false>
                legacy=<true|false>
                prefixtypes=<true|false>
                undeftags=<true|false>
                """
            );
        } else {
            Executor executor = new Executor(args);
            executor.execute();
        }
    }

}
