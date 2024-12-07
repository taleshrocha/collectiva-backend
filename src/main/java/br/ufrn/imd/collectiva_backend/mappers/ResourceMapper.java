package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.utils.Parser;
import org.springframework.stereotype.Component;


@Component
public class ResourceMapper implements DTOMapper<Resource, ResourceDTO> {
    private final EventMapper eventMapper;

    public ResourceMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public ResourceDTO toDTO(Resource entity) {
        return new ResourceDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.getLog(), entity.getBannerId(), entity.getEvent() != null ? eventMapper.toDTO(entity.getEvent()) : null);
    }

    @Override
    public Resource toEntity(ResourceDTO entityDTO) {
        return Parser.parse(entityDTO, Resource.class);
    }
}
