package net.justgnat.mcfdoc;

import java.io.PrintStream;

public class Logger {

    private final PrintStream stream;

    private static final Logger logger = new Logger(System.out);

    private Logger(PrintStream stream) {
        this.stream = stream;
    }

    public static void write(Object object) {
        logger.log(object);
    }

    public void log(Object object) {
        stream.println(object);
    }

}
