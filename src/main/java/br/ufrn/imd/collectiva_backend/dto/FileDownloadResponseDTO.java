package br.ufrn.imd.collectiva_backend.dto;

import org.springframework.core.io.Resource;

/**
 * Represents a response for downloading a file.
 * <p>
 * This record includes the file name and the corresponding Resource for
 * download.
 *
 * @param fileName The name of the file being downloaded.
 * @param resource The Resource containing the file data.
 */
public record FileDownloadResponseDTO(
        String fileName,
        Resource resource) {
}
