package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.EventDTO;
import br.ufrn.imd.collectiva_backend.model.Event;
import br.ufrn.imd.collectiva_backend.model.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EventMapper implements DTOMapper<Event, EventDTO> {
    private final ResourceMapper resourceMapper;

    public EventMapper(@Lazy ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    public EventDTO toDTO(Event entity) {
        return new EventDTO(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCategory(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDescription(),
                entity.getBannerId(),
                entity.getResources() != null ? resourceMapper.toDTOWithoutEvent(entity.getResources()) : null,
                entity.getIsFinished()
        );
    }

    public EventDTO toDTOWithoutResource(Event entity) {
        return new EventDTO(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCategory(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDescription(),
                entity.getBannerId(),
                null,
                entity.getIsFinished()
        );
    }

    @Override
    public Event toEntity(EventDTO entityDTO) {
        Event entity = new Event();

        entity.setId(entityDTO.id());
        entity.setName(entityDTO.name());
        entity.setLocation(entityDTO.location());
        entity.setCategory(entityDTO.category());
        entity.setStartDate(entityDTO.startDate());
        entity.setEndDate(entityDTO.endDate());
        entity.setDescription(entityDTO.description());
        entity.setBannerId(entityDTO.bannerId());

        if (entityDTO.resources() != null) {
            List<Resource> resources = entityDTO.resources().stream().map(resourceDTO -> {
                Resource resource = new Resource();
                resource.setId(resourceDTO.id());
                return resource;
            }).toList();

            entity.setResources(resources);
        }

        entity.setIsFinished(entityDTO.isFinished());

        return entity;
    }
}
