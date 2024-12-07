package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.OrganizationDTO;
import br.ufrn.imd.collectiva_backend.model.Organization;
import br.ufrn.imd.collectiva_backend.utils.Parser;
import org.springframework.stereotype.Component;


@Component
public class OrganizationMapper implements DTOMapper<Organization, OrganizationDTO> {

    private final UserInfoMapper userInfoMapper;

    public OrganizationMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public OrganizationDTO toDTO(Organization entity) {
        return new OrganizationDTO(
                entity.getId(),
                entity.getName(),
                entity.getUsers() != null ? userInfoMapper.toDTO(entity.getUsers()) : null);
    }

    @Override
    public Organization toEntity(OrganizationDTO entityDTO) {
        return Parser.parse(entityDTO, Organization.class);
    }
}
