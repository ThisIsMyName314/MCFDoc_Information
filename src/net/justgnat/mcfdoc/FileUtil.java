package net.justgnat.mcfdoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileUtil {

    public static List<String> read(File file) {
        try {
            return Files.readAllLines(file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            return List.of();
        }
    }

    public static File getSubFile(File parent, String childName, boolean isDirectory)
            throws RuntimeException {
        File file = new File(parent.getAbsolutePath() + File.separatorChar + childName);
        if (!file.exists() || file.isDirectory() != isDirectory)
            return null;
        return file;
    }

    public static File[] getSubFilesOrThrow(File parent, String errorMessage)
            throws RuntimeException {
        File[] subs = parent.listFiles();
        if (subs == null) {
            throw new RuntimeException(errorMessage);
        }
        return subs;
    }

}
