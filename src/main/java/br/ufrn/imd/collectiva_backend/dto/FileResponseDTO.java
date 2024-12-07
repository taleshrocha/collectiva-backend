package br.ufrn.imd.collectiva_backend.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a Data Transfer Object (DTO) for file information.
 * <p>
 * This record includes the unique identifier, name, and description of a file.
 *
 * @param id          The unique identifier of the file.
 * @param name        The name of the file.
 * @param description A description of the file (optional).
 */
public record FileResponseDTO(
        long id,
        @NotBlank String name,
        String description)

        implements Serializable {
}
