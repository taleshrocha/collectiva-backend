package br.ufrn.imd.collectiva_backend.dto;

public record ResourceDTO(
        Long id,
        String description,
        String log,
        Long bannerId
) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
