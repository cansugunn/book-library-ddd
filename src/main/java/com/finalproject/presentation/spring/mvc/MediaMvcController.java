package com.finalproject.presentation.spring.mvc;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/mvc/media")
public class MediaMvcController {
    private static final Path DEFAULT_IMAGE_PATH = Path.of(
            "src",
            "main",
            "resources",
            "static",
            "resources",
            "images",
            "placeholder-cover.svg"
    );

    @GetMapping("/cover")
    @ResponseBody
    public ResponseEntity<Resource> mediaCover(@RequestParam("path") String path) {
        if (path == null || path.isEmpty()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(DEFAULT_IMAGE_PATH));
        }

        try {
            Path cover = Path.of(path).normalize().toAbsolutePath();
            if (!Files.exists(cover) || !Files.isRegularFile(cover)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new FileSystemResource(DEFAULT_IMAGE_PATH));
            }

            String type = Files.probeContentType(cover);
            MediaType mediaType = type == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(type);
            return ResponseEntity.ok().contentType(mediaType).body(new FileSystemResource(cover));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
