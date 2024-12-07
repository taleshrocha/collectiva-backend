package br.ufrn.imd.collectiva_backend.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a Data Transfer Object (DTO) for file upload.
 * <p>
 * This record includes the uploaded file and an optional description.
 *
 * @param file        The MultipartFile representing the uploaded file (must not
 *                    be null).
 * @param description An optional description of the uploaded file.
 */
public record FileUploadDTO(
        @NotNull MultipartFile file,
        String description)

        implements Serializable {
}
