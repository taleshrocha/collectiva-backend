package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ResourceMapper implements DTOMapper<Resource, ResourceDTO> {
    private final EventMapper eventMapper;

    private final ResourceHistoryMapper resourceHistoryMapper;

    public ResourceMapper(EventMapper eventMapper, ResourceHistoryMapper resourceHistoryMapper) {
        this.eventMapper = eventMapper;
        this.resourceHistoryMapper = resourceHistoryMapper;
    }

    @Override
    public ResourceDTO toDTO(Resource entity) {
        return new ResourceDTO(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getResourceHistory() != null ? resourceHistoryMapper.toDTO(entity.getResourceHistory()) : null,
                entity.getBannerId(),
                entity.getEvent() != null ? eventMapper.toDTOWithoutResource(entity.getEvent()) : null
        );
    }

    public ResourceDTO toDTOWithoutEvent(Resource entity) {
        return new ResourceDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getResourceHistory() != null ? resourceHistoryMapper.toDTO(entity.getResourceHistory()) : null,
                entity.getBannerId(),
                null
        );
    }

    public List<ResourceDTO> toDTOWithoutEvent(List<Resource> entity) {
        return entity.stream().map(this::toDTOWithoutEvent).toList();
    }

    @Override
    public Resource toEntity(ResourceDTO entityDTO) {

        Resource entity = new Resource();

        entity.setId(entityDTO.id());
        entity.setName(entityDTO.name());
        entity.setDescription(entityDTO.description());
        entity.setResourceHistory(entityDTO.resourceHistory() != null ? resourceHistoryMapper.toEntity(entityDTO.resourceHistory()) : null);
        entity.setBannerId(entityDTO.bannerId());
        entity.setEvent(entityDTO.event() != null ? eventMapper.toEntity(entityDTO.event()) : null);

        return entity;
    }
}
