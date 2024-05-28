package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

@UtilityClass
public class FileUtil {

    public Optional<File> getOldestFile(final File filePath) {
        final Path path = filePath.toPath();

        Optional<File> file = Optional.empty();

        try {
            final long fileCount = Files.list(path).count();

            if (fileCount > 1) {
                Optional<Path> oldestFilePath = Files.list(path)
                        .filter(f -> !Files.isDirectory(f))
                        .min(Comparator.comparingLong(f -> f.toFile().lastModified()));

                if (oldestFilePath.isPresent()) {
                    file = Optional.of(oldestFilePath.get().toFile());
                }
            }
        } catch (IOException e) {
            BetterLogger.warn("Failed to get latest file from " + filePath.getAbsolutePath() + ". Error: " + e);
        }

        return file;
    }
}
