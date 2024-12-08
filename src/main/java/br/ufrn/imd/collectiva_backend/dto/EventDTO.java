package br.ufrn.imd.collectiva_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDTO(
        Long id,
        String name,
        String location,
        String category,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String description,
        Long bannerId,
        List<ResourceDTO> resources
) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
