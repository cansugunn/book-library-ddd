package com.finalproject.infrastructure.common.fms;

import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileStorageAdapter {

    public String toStorageKey(String localCoverPath) {
        if (localCoverPath == null || localCoverPath.isBlank()) {
            return null;
        }

        Path path = Path.of(localCoverPath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return null;
        }

        return path.toAbsolutePath().toString();
    }
}
