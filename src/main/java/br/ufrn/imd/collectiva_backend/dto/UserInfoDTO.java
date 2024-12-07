package br.ufrn.imd.collectiva_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfoDTO(
        Long id,
        String email,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        String alternativeEmail,
        PersonDTO person
) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
