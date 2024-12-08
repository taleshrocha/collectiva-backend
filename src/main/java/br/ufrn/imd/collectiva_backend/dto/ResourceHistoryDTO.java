package br.ufrn.imd.collectiva_backend.dto;

import java.time.ZonedDateTime;

public record ResourceHistoryDTO(
        Long id,
        String description,
        ZonedDateTime createdAt
) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
