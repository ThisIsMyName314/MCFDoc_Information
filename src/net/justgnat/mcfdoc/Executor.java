package net.justgnat.mcfdoc;

import net.justgnat.mcfdoc.parser.Datapack;
import net.justgnat.mcfdoc.parser.DatapackParser;
import net.justgnat.mcfdoc.parser.Printer;
import net.justgnat.mcfdoc.parser.TypeManager;

public class Executor {

    private final Options options;

    private final TypeManager typeManager;

    public Executor(String[] arguments) {
        this.options = parseOptions(arguments);
        this.typeManager = new TypeManager();
    }

    private Options parseOptions(String[] arguments) {
        try {
            return OptionParser.parse(arguments);
        } catch (RuntimeException e) {
            Logger.write(e.getMessage());
            return null;
        }
    }

    public void execute() {
        if (options != null) {
            try {
                Datapack datapack = DatapackParser.parseDatapack(options, typeManager);
                Printer.printDatapack(datapack, options, typeManager);
            } catch (RuntimeException e) {
                Logger.write(e.getMessage());
            }
        }
    }

}
