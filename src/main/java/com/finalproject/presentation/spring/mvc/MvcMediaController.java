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
public class MvcMediaController {

    @GetMapping("/cover")
    @ResponseBody
    public ResponseEntity<Resource> mediaCover(@RequestParam("path") String path) {
        try {
            Path cover = Path.of(path).normalize().toAbsolutePath();
            if (!Files.exists(cover) || !Files.isRegularFile(cover)) {
                return ResponseEntity.notFound().build();
            }
            String type = Files.probeContentType(cover);
            MediaType mediaType = type == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(type);
            return ResponseEntity.ok().contentType(mediaType).body(new FileSystemResource(cover));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
