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
import java.util.Optional;

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

    private static final ResponseEntity<Resource> DEFAULT_IMAGE_RESPONSE =
            ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/svg+xml"))
                    .body(new FileSystemResource(DEFAULT_IMAGE_PATH));

    @GetMapping("/cover")
    @ResponseBody
    public ResponseEntity<Resource> mediaCover(@RequestParam("path") String path) {
        boolean isValid = Optional.ofNullable(path)
                .filter(s -> !s.isEmpty())
                .map(Path::of)
                .map(Path::normalize)
                .map(Path::toAbsolutePath)
                .filter(Files::isRegularFile)
                .map(Files::isReadable)
                .orElse(false);

        if (!isValid) {
            return DEFAULT_IMAGE_RESPONSE;
        }

        try {
            Path cover = Path.of(path).normalize().toAbsolutePath();
            String type = Files.probeContentType(cover);
            MediaType mediaType = type == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(type);
            return ResponseEntity.ok().contentType(mediaType).body(new FileSystemResource(cover));
        } catch (Exception e) {
            return DEFAULT_IMAGE_RESPONSE;
        }
    }
}
