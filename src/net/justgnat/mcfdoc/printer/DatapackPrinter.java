package net.justgnat.mcfdoc.printer;

import net.justgnat.mcfdoc.Logger;
import net.justgnat.mcfdoc.Options;
import net.justgnat.mcfdoc.parser.*;
import net.justgnat.mcfdoc.sanitiser.HtmlSanitiser;
import static net.justgnat.mcfdoc.printer.HtmlUtil.*;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DatapackPrinter {

    /*
     * Accessible, dark-by-default theme.
     *
     * Goals (WCAG 2.1 AA and better): rem-based typography so the document
     * respects the reader's browser font size, body text contrast of at least
     * 4.5:1 in both colour schemes, visible keyboard focus, and a print
     * stylesheet that falls back to black-on-white. Colours are driven by CSS
     * custom properties; the dark palette is the default and a light palette is
     * supplied for readers whose system prefers light. All values are inlined so
     * the generated document stays a single self-contained file.
     */
    public static final String STYLES = """
            <style>
            :root {
              color-scheme: dark light;
              --bg: #12151a;
              --surface: #1b1f27;
              --surface-alt: #232833;
              --surface-stripe: #1f242d;
              --border: #3a414e;
              --text: #e7e9ee;
              --muted: #b8bfca;
              --heading: #f3f5f8;
              --link: #7cc4ff;
              --link-visited: #c3a8ff;
              --link-hover: #ffb454;
              --type: #dc9bff;
              --deprecated: #ff6b6b;
              --hidden: #ffc93c;
              --focus: #ffb454;
            }
            @media (prefers-color-scheme: light) {
              :root {
                --bg: #ffffff;
                --surface: #f7f8fa;
                --surface-alt: #e6eaf0;
                --surface-stripe: #eef1f5;
                --border: #c4ccd6;
                --text: #1a1d22;
                --muted: #454b54;
                --heading: #10131a;
                --link: #14487a;
                --link-visited: #5b3ea6;
                --link-hover: #a34b00;
                --type: #7a1fa0;
                --deprecated: #b3160f;
                --hidden: #8a6400;
                --focus: #14487a;
              }
            }
            html { font-size: 112.5%; }
            body {
              margin: 0 auto;
              max-width: 72rem;
              padding: 1.5rem 1.25rem 3rem;
              background-color: var(--bg);
              color: var(--text);
              font-family: Arial, Helvetica, sans-serif;
              font-size: 1.05rem;
              line-height: 1.6;
            }
            h1, h2 { color: var(--heading); font-style: italic; line-height: 1.25; }
            h1 { font-size: 2rem; }
            h2 { font-size: 1.5rem; }
            h5 { font-family: Verdana, sans-serif; font-size: 1rem; line-height: 1.4; margin: 0.75rem 0 0.25rem; color: var(--heading); }
            p { margin: 0.5rem 0; }
            a { color: var(--link); text-decoration: none; }
            a:visited { color: var(--link-visited); }
            a:hover { color: var(--link-hover); text-decoration: underline; }
            a:focus-visible { outline: 3px solid var(--focus); outline-offset: 2px; border-radius: 2px; }
            .skip-link {
              position: absolute; left: -9999px; top: 0;
              background: var(--surface-alt); color: var(--text);
              padding: 0.5rem 1rem; border: 2px solid var(--focus); border-radius: 4px; z-index: 10;
            }
            .skip-link:focus { left: 1rem; top: 1rem; }
            .block {
              border: 1px solid var(--border);
              border-radius: 6px;
              padding: 0.75rem 1rem;
              margin: 1.25rem 0;
              background-color: var(--surface);
            }
            .tabletitle { font-weight: bold; font-style: italic; font-size: 1.35rem; color: var(--heading); }
            .methodname { font-family: 'DejaVu Sans Mono', 'Courier New', monospace; font-size: 0.95rem; }
            .methodheader { font-family: 'DejaVu Sans Mono', 'Courier New', monospace; background-color: var(--surface-alt); }
            .type { color: var(--type); font-weight: bold; }
            .anno-deprecated { color: var(--deprecated); font-weight: bold; }
            .anno-hidden { color: var(--hidden); font-weight: bold; }
            table { border-collapse: collapse; width: 100%; background-color: transparent; }
            caption { text-align: left; }
            td, th { text-align: left; padding: 0.5rem 0.75rem; border-bottom: 1px solid var(--border); vertical-align: top; }
            th { background-color: var(--surface-alt); color: var(--heading); }
            tr:nth-child(odd) td { background-color: var(--surface-stripe); }
            @media print {
              :root {
                --bg: #ffffff; --surface: #ffffff; --surface-alt: #ffffff; --surface-stripe: #ffffff;
                --border: #666666; --text: #000000; --muted: #222222; --heading: #000000;
                --link: #000000; --link-visited: #000000; --link-hover: #000000;
                --type: #000000; --deprecated: #000000; --hidden: #000000; --focus: #000000;
              }
              body { max-width: none; }
              a { text-decoration: underline; }
              .type, .anno-deprecated, .anno-hidden { font-weight: bold; }
            }
            </style>""";

    private final Datapack datapack;

    private final TypeManager typeManager;

    private final Options options;

    public DatapackPrinter(Datapack datapack, Options options, TypeManager typeManager) {
        this.datapack = datapack;
        this.typeManager = typeManager;
        this.options = options;
    }

    /**
     * Attempts to print the given Datapack. Does not indicate whether it succeeds or not.
     */
    public static void printDatapack(Datapack datapack, Options options, TypeManager typeManager) {
        DatapackPrinter printer = new DatapackPrinter(datapack, options, typeManager);
        printer.print();
    }

    private void print() {
        if (!checkPath(options.outputPath, options.canOverwrite))
            return;

        try (FileWriter writer = new FileWriter(options.outputPath)) {
            writer.write(generateHtml());
            Logger.write("Wrote to " + options.outputPath);
        } catch (Exception e) {
            Logger.write("Failed Print: " + e);
        }
    }

    private boolean checkPath(String path, boolean canOverwrite) {
        File file = new File(path);
        if (!file.exists())
            return true;

        if (!canOverwrite) {
            if (!file.isDirectory()) {
                Logger.write("File already exists! Use the flag [overwrite=<true|false>] to specify whether overwriting is permitted");
            } else {
                Logger.write("Can not overwrite a directory");
            }
            return false;
        }
        return true;
    }

    private String generateHtml() {
        String header =
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                        "<title>" + datapack.name() + "</title>" +
                        STYLES +
                        "</head>" +
                        "<body>" +
                        "<a class=\"skip-link\" href=\"#content\">Skip to content</a>" +
                        "<main id=\"content\">";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String credits = "Automatically generated by " + "<a href=\"https://github.com/ThisIsMyName314/MCFDoc_Information\" style=\"all:revert\">MCFDoc</a>" +
                " on " + format.format(Date.from(Instant.now()));

        String footer = "</main>" + h5(credits) + "</body></html>";

        String title = h1(datapack.name());
        String subtitle = (datapack.hasVersion() ? datapack.version() : "");
        if (datapack.hasAuthors()) subtitle += " by " + datapack.author();

        return header +
                title + // Do not need to sanitise as these are provided at the command line
                h2(subtitle) +
                p(datapack.description()) +
                generateTypeBlock() +
                generateFunctionSummary() +
                generateFunctionDetail() +
                footer;
    }

    private String generateTypeBlock() {
        if (typeManager.getCustomTypes().isEmpty())
            return "";

        return div("block",
                h2("tabletitle", "Declared Types") +
                        table(
                            tr(th("Name") + th("Description")) +
                               generateTypeRows()
                        )
        );
    }

    private String generateTypeRows() {
        StringBuilder sb = new StringBuilder();
        for (CustomType type : typeManager.getCustomTypes()) {
            sb.append("<tr>")
                    .append(td(sanitise(type.name())))
                    .append(td(sanitise(type.description())))
                    .append("</tr>");
        }
        return sb.toString();
    }

    private String generateFunctionSummary() {
        if (datapack.groups().isEmpty())
            return "";

        return div("block",
                h2("tabletitle", "Function Summary") +
                        table(
                            tr(th("Name") + th("Description")) +
                                    generateFunctionSummaryRows()
                        )
        );
    }

    private String generateFunctionSummaryRows() {
        StringBuilder sb = new StringBuilder();
        for (FunctionGroup module : datapack.groups()) {
            for (Function function : module.functions()) {
                if (function.isHidden() && !options.showHidden)
                    continue;
                sb.append("<tr>");
                sb.append(td(a("#" + function.name(), function.name())));
                sb.append(td(sanitise(
                        getSingleLineDescription(function.description())
                )));
                sb.append("</tr>");
            }
        }
        return sb.toString();
    }

    private String getSingleLineDescription(String string) {
        int lineBreak = string.indexOf("<br>");
        if (lineBreak == -1) {
            if (string.length() < 100)
                return string;
            return string.substring(0, 97) + "...";
        }
        return string.substring(0, lineBreak);
    }

    private String generateFunctionDetail() {
        if (datapack.groups().isEmpty())
            return "";

        return div("block",
                h2("tabletitle", "Function Detail") +
                        table(
                                generateFunctionDetailRows()
                        )
        );
    }

    private String generateFunctionDetailRows() {
        StringBuilder sb = new StringBuilder();

        for (FunctionGroup group : datapack.groups()) {
            for (Function function : group.functions()) {
                if (function.isHidden() && !options.showHidden)
                    continue;

                sb.append("<tr> <th class=\"methodheader\" id=\"")
                        .append(function.name())
                        .append("\">")
                        .append(function.simpleName())
                        .append("</th> </tr>");
                sb.append("<tr> <td> <p class=\"methodname\"> ")
                        .append(generateFunctionString(function)).append("</p>");
                sb.append(sanitise(function.description()));
                sb.append(generateParameterBlock(function));
                sb.append(generateTagBlock(function));
            }
        }

        return sb.toString();
    }

    private String sanitise(String content) {
        return HtmlSanitiser.sanitise(content, options.html);
    }

    private String generateTagBlock(Function function) {
        Collection<List<Annotation>> tagTypes = function.annotations().getAnnotations();
        if (tagTypes.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();
        for (List<Annotation> annotationList : tagTypes) {
            /* Tag title */
            sb.append(h5(sanitise(annotationList.getFirst().getName()) + ":"));
            /* Tag body */
            sb.append("<p>");
            for (Annotation annotation : annotationList) {
                if (!annotation.getBody().isEmpty()) {
                    sb.append(sanitise(annotation.getBody()));
                    sb.append("<br>");
                }
            }
            sb.append("</p>");
        }
        return sb.toString();
    }

    private String generateParameterBlock(Function function) {
        if (function.parameters().isEmpty())
            return "";

        String title = h5("Parameters:");
        StringBuilder sb = new StringBuilder("<p>");
        for (Parameter param : function.parameters()) {
            sb.append(sanitise(param.name()));
            if (!param.info().isEmpty()) {
                sb.append(" - ").append(sanitise(param.info()));
            }
            sb.append("<br>");
        }
        sb.append("</p>");
        return title + sb;
    }

    private String generateFunctionString(Function function) {
        StringBuilder sb = new StringBuilder();
        if (function.isDeprecated()) {
            sb.append("<span class=\"anno-deprecated\">@Deprecated</span><br>");
        }
        if (function.isHidden()) {
            sb.append("<span class=\"anno-hidden\">@Hidden</span><br>");
        }
        sb.append(function.name()).append("(");
        int i = 0;
        for (Parameter param : function.parameters()) {
            ++i;
            if (options.prefixTypesInOutput) {
                sb.append(param.type().getColouredName()).append(" ").append(sanitise(param.name()));
            } else {
                sb.append(sanitise(param.name())).append(": ").append(param.type().getColouredName());
            }
            if (i != function.parameters().size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
