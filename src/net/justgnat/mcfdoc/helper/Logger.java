package net.justgnat.mcfdoc.helper;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    public enum LogType {
        DEFAULT, WARN, ERROR
    }

    private final PrintStream stream;

    private List<LoggerListener> listeners;

    private static final Logger logger = new Logger(System.out);

    private Logger(PrintStream stream) {
        this.stream = stream;
        this.listeners = null;
    }

    public static void addListener(LoggerListener listener) {
        if (logger.listeners == null)
            logger.listeners = new ArrayList<>(8);
        logger.listeners.add(listener);
    }

    public void log(Object o) {
        stream.println(o);
    }

    public static void warn(Object o) {
        logger.log(o);
        fire(LogType.WARN, o);
    }

    public static void write(Object o) {
        logger.log(o);
        fire(LogType.DEFAULT, o);
    }

    public static void error(Object o) {
        logger.log(o);
        fire(LogType.ERROR, o);
    }

    private static void fire(LogType type, Object o) {
        if (logger.listeners != null) {
            String s = o.toString();
            for (LoggerListener listener : logger.listeners) {
                listener.onMessage(type, s);
            }
        }
    }

}
