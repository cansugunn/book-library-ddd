package com.finalproject.infrastructure.common.fms;

import com.finalproject.application.ports.output.fms.FileManagementService;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileManagementService implements FileManagementService {

    @Override
    public String toPublicCoverUrl(String localCoverPath) {
        if (localCoverPath == null || localCoverPath.isBlank()) {
            return "/resources/images/placeholder-cover.svg";
        }

        Path path = Path.of(localCoverPath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "/resources/images/placeholder-cover.svg";
        }

        return "/mvc/media/cover?path=" + UriUtils.encode(path.toAbsolutePath().toString(), StandardCharsets.UTF_8);
    }
}
