package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.utils.Parser;
import org.springframework.stereotype.Component;


@Component
public class ResourceMapper implements DTOMapper<Resource, ResourceDTO> {

    @Override
    public ResourceDTO toDTO(Resource entity) {
        return new ResourceDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLog(),
                entity.getBannerId()
        );
    }

    @Override
    public Resource toEntity(ResourceDTO entityDTO) {
        return Parser.parse(entityDTO, Resource.class);
    }
}
