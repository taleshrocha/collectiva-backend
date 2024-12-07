package br.ufrn.imd.collectiva_backend.utils;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for file-related operations.
 * <p>
 * This component provides methods for deleting files, copying files, and
 * creating URL resources.
 */
@Component
public class FileUtil {

    /**
     * Deletes a file at the specified file path.
     *
     * @param filePath The path of the file to be deleted.
     * @throws IOException If an error occurs while deleting the file.
     */
    public void deleteFile(Path filePath) throws IOException {
        Files.deleteIfExists(filePath);
    }

    /**
     * Copies an uploaded file to the specified file path, replacing it if it
     * already exists.
     *
     * @param uploadFile The MultipartFile representing the uploaded file.
     * @param filePath   The path where the file should be copied.
     * @throws IOException If an error occurs during the file copying process.
     */
    public void copyFile(MultipartFile uploadFile, Path filePath) throws IOException {
        Files.copy(uploadFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Creates a URL resource based on the provided URI.
     *
     * @param uri The URI from which to create the URL resource.
     * @return A UrlResource representing the resource at the provided URI.
     * @throws IOException If an error occurs while creating the URL resource.
     */
    public UrlResource createUrlResource(URI uri) throws IOException {
        return new UrlResource(uri);
    }
}
