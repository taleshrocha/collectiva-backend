package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ResourceMapper implements DTOMapper<Resource, ResourceDTO> {
    private final EventMapper eventMapper;

    public ResourceMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public ResourceDTO toDTO(Resource entity) {
        return new ResourceDTO(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLog(),
                entity.getBannerId(),
                entity.getEvent() != null ? eventMapper.toDTOWithoutResource(entity.getEvent()) : null
        );
    }

    public ResourceDTO toDTOWithoutEvent(Resource entity) {
        return new ResourceDTO(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLog(),
                entity.getBannerId(),
                null
        );
    }

    public List<ResourceDTO> toDTOWithoutEvent(List<Resource> entity) {
        return entity.stream().map(this::toDTOWithoutEvent).toList();
    }

    @Override
    public Resource toEntity(ResourceDTO entityDTO) {

        Resource resource = new Resource();

        resource.setName(entityDTO.name());
        resource.setDescription(entityDTO.description());
        resource.setLog(entityDTO.log());
        resource.setBannerId(entityDTO.bannerId());
        resource.setEvent(entityDTO.event() != null ? eventMapper.toEntity(entityDTO.event()) : null);

        return resource;

    }
}
