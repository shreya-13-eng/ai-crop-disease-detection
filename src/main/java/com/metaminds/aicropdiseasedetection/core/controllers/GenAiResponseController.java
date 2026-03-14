package com.metaminds.aicropdiseasedetection.core.controllers;

import com.metaminds.aicropdiseasedetection.core.services.GenAiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class GenAiResponseController {

    final GenAiResponseService genAiResponseService;

    public GenAiResponseController(GenAiResponseService genAiResponseService) {
        this.genAiResponseService = genAiResponseService;
    }

    @GetMapping("/hello/{prompt}")
    public ResponseEntity<?> hello(
            @PathVariable String prompt
    ) {
        return new ResponseEntity<>(genAiResponseService.getGeminiResponse(prompt), HttpStatus.OK);
    }

    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> predictDisease(
            @RequestPart("image") MultipartFile image
    ) {
        try {
            Path uploadPath = Paths.get("upload").toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalName = image.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String uuidFileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(uuidFileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>(genAiResponseService.predictDisease(image), HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

}
