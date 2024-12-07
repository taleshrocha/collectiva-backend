package br.ufrn.imd.collectiva_backend.dto;

import java.util.List;

public record OrganizationDTO(
        Long id,
        String name,
        List<UserInfoDTO> users
) implements EntityDTO {
    @Override
    public EntityDTO toResponse() {
        return this;
    }
}
