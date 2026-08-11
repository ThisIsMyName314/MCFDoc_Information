package net.justgnat.mcfdoc;

import java.io.PrintStream;

public class Logger {

    private final PrintStream stream;

    private static final Logger LOGGER = new Logger(System.out);

    private Logger(PrintStream stream) {
        this.stream = stream;
    }

    public static void warn(Object o) {
        LOGGER.log(o);
    }

    public static void write(Object o) {
        LOGGER.log(o);
    }

    public static void error(Object o) {
        LOGGER.log(o);
    }

    public void log(Object o) {
        stream.println(o);
    }

}
