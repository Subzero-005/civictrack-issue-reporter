package com.civictrack.issuetracker.service;

import com.civictrack.issuetracker.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadsRoot;

    public FileStorageService(@Value("${app.uploads.dir}") String uploadsDir) {
        this.uploadsRoot = Path.of(uploadsDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadsRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create uploads directory", e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only JPEG, PNG, or WEBP images are allowed");
        }

        String extension = switch (file.getContentType()) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };

        String filename = UUID.randomUUID() + extension;
        Path target = uploadsRoot.resolve(filename);

        try {
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store uploaded file");
        }

        return "/uploads/" + StringUtils.cleanPath(filename);
    }
}
