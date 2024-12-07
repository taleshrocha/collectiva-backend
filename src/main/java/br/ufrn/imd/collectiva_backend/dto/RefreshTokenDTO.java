package br.ufrn.imd.collectiva_backend.dto;

public record RefreshTokenDTO(
        String refreshToken) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
