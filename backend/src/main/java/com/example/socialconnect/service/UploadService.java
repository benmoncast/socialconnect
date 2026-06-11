package com.example.socialconnect.service;

import com.example.socialconnect.exception.AppException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadRoot;

    public UploadService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Please choose an image file to upload");
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT)
                : "";

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Only JPG, JPEG, PNG, and WEBP files are allowed");
        }

        validateImageContent(file, extension);

        try {
            Files.createDirectories(uploadRoot);
            String fileName = UUID.randomUUID() + "." + extension;
            storeSanitizedImage(file, extension, uploadRoot.resolve(fileName));
            return "/uploads/" + fileName;
        } catch (IOException ex) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store uploaded file");
        }
    }

    private void validateImageContent(MultipartFile file, String extension) {
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_CONTENT_TYPES.contains(contentType) || !contentTypeMatchesExtension(contentType, extension)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Only valid JPG, JPEG, PNG, and WEBP images are allowed");
        }

        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = inputStream.readNBytes(12);
            if (!signatureMatchesExtension(header, extension)) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Only valid JPG, JPEG, PNG, and WEBP images are allowed");
            }
        } catch (IOException ex) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Could not read uploaded image");
        }
    }

    private void storeSanitizedImage(MultipartFile file, String extension, Path destination) throws IOException {
        if ("webp".equals(extension)) {
            Files.copy(file.getInputStream(), destination);
            return;
        }

        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Only valid JPG, JPEG, PNG, and WEBP images are allowed");
        }

        String format = "png".equals(extension) ? "png" : "jpg";
        BufferedImage sanitized = "png".equals(format) ? image : toRgbImage(image);
        try (OutputStream outputStream = Files.newOutputStream(destination)) {
            if (!ImageIO.write(sanitized, format, outputStream)) {
                throw new IOException("No image writer available for uploaded image");
            }
        }
    }

    private BufferedImage toRgbImage(BufferedImage source) {
        BufferedImage rgbImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgbImage.createGraphics();
        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, source.getWidth(), source.getHeight());
            graphics.drawImage(source, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        return rgbImage;
    }

    private boolean contentTypeMatchesExtension(String contentType, String extension) {
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg".equals(contentType);
            case "png" -> "image/png".equals(contentType);
            case "webp" -> "image/webp".equals(contentType);
            default -> false;
        };
    }

    private boolean signatureMatchesExtension(byte[] header, String extension) {
        return switch (extension) {
            case "jpg", "jpeg" -> startsWith(header, 0xFF, 0xD8, 0xFF);
            case "png" -> startsWith(header, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A);
            case "webp" -> startsWith(header, 0x52, 0x49, 0x46, 0x46)
                    && header.length >= 12
                    && header[8] == 0x57
                    && header[9] == 0x45
                    && header[10] == 0x42
                    && header[11] == 0x50;
            default -> false;
        };
    }

    private boolean startsWith(byte[] header, int... signature) {
        if (header.length < signature.length) {
            return false;
        }

        for (int i = 0; i < signature.length; i += 1) {
            if ((header[i] & 0xFF) != signature[i]) {
                return false;
            }
        }
        return true;
    }
}
