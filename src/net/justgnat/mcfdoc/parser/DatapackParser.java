package net.justgnat.mcfdoc.parser;

import net.justgnat.mcfdoc.FileUtil;
import net.justgnat.mcfdoc.Logger;
import net.justgnat.mcfdoc.Options;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class DatapackParser {

    public static final String EXTENSION = ".mcfunction";

    public static final String TYPES_NAME = "types";

    private final TypeManager typeManager;

    private final Datapack.Builder datapack = new Datapack.Builder();

    private FunctionGroup.Builder functionBuilder = new FunctionGroup.Builder();

    private final List<FunctionGroup> functionGroups = new LinkedList<>();

    private final Options options;

    public DatapackParser(Options options, TypeManager typeManager) {
        this.options = options;
        this.typeManager = typeManager;
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }

    /**
     * @throws RuntimeException if an error occurs. Can be either due to File parsing or invalid MCFDoc comments.
     */
    public static Datapack parseDatapack(Options options, TypeManager typeManager) {
        DatapackParser parser = new DatapackParser(options, typeManager);
        return parser.parse();
    }

    private Datapack parse() throws RuntimeException {
        datapack.author = options.author;
        datapack.description = options.description;
        datapack.version = options.version;

        File datapackFile = new File(options.datapackPath);

        datapack.name = datapackFile.getName();

        File dataDirectory = FileUtil.getSubFile(datapackFile, "data", true);
        if (dataDirectory == null) {
            error("Failed to find data directory");
        }

        parseDataDir(dataDirectory);

        datapack.groups = functionGroups;
        return datapack.build();
    }

    private void parseDataDir(File dataFile) {
        tryParseTypesFile(dataFile);

        File[] files = FileUtil.getSubFilesOrThrow(dataFile, "Failed to read data directory");
        for (File sub : files) {
            if (sub.isDirectory() &&
                    !sub.getName().equals("minecraft")) {
                parseNamedSection(sub);
                functionGroups.add(functionBuilder.build());
                functionBuilder = new FunctionGroup.Builder();
            }
        }
    }

    private void tryParseTypesFile(File dataFile) {
        File typesFile = new File(dataFile.getAbsolutePath() + "/" + TYPES_NAME);
        if (typesFile.exists()) {
            parseTypesFile(typesFile);
        }
    }

    private void parseTypesFile(File file) {
        try {
            Logger.write("Reading types file...");
            List<String> lines = Files.readAllLines(file.toPath());
            TypeParser parser = new TypeParser(lines, typeManager);
            parser.parse();
        } catch (Exception e) {
            error("Failed to read types file: " + e.getMessage());
        }
    }

    private void parseNamedSection(File file) {
        String namespace = file.getName() + ":";

        File[] files = FileUtil.getSubFilesOrThrow(file, "Failed to read directory " + file.getAbsolutePath());
        final String functionFolder = options.legacyNames ? "functions" : "function";

        for (File sub : files) {
            if (sub.isDirectory() &&
                    sub.getName().equals(functionFolder)) {
                parseFunctions(namespace, sub);
            }
        }
    }

    private void parseFunctions(String namespace, File parent) {
        Logger.write("Parsing functions in " + parent.getAbsolutePath());

        File[] files = FileUtil.getSubFilesOrThrow(parent, "Failed to read directory " + parent.getAbsolutePath());

        for (File file : files) {
            if (file.isDirectory()) {
                parseFunctions(namespace + file.getName() + "/", file);
            } else {
                parseFile(namespace, file);
            }
        }
    }

    private void parseFile(String namespace, File file) {
        if (!isFunction(file))
            return;

        String functionName = getFunctionName(file);
        if (!isValidName(functionName)) {
            Logger.write("Skipping function with invalid name '" + functionName + "'");
            return;
        }

        functionBuilder.namespace = namespace.substring(0, namespace.length() - 1);
        String name = namespace + functionName;
        FunctionParser parser = new FunctionParser(name, FileUtil.read(file), options.undefinedTags, typeManager);

        try {
            functionBuilder.functions.add(parser.parse());
        } catch (Exception e) {
            error("Error whilst parsing: " + e.getMessage());
        }

        Logger.write("Parsed " + file.getName());
    }

    private String getFunctionName(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.length() - (EXTENSION.length()));
    }

    private boolean isFunction(File file) {
        return file.getName().endsWith(EXTENSION);
    }

    private boolean isValidName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char chr = name.charAt(i);
            if ((chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z') || (chr >= '0' && chr <= '9') || chr == '_')
                continue;
            return false;
        }
        return true;
    }

}
