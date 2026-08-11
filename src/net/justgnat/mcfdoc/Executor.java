package net.justgnat.mcfdoc;

import net.justgnat.mcfdoc.parser.Datapack;
import net.justgnat.mcfdoc.parser.DatapackParser;
import net.justgnat.mcfdoc.parser.TypeManager;
import net.justgnat.mcfdoc.printer.DatapackPrinter;

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
            Logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * @return true only if the generation occurred with no errors
     */
    public boolean execute() {
        if (options != null) {
            try {
                Datapack datapack = DatapackParser.parseDatapack(options, typeManager);
                return DatapackPrinter.printDatapack(datapack, options, typeManager);
            } catch (RuntimeException e) {
                Logger.error(e.getMessage());
            }
        }
        return false;
    }

}
