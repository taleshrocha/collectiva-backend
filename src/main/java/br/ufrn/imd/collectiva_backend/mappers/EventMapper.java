package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.EventDTO;
import br.ufrn.imd.collectiva_backend.model.Event;
import br.ufrn.imd.collectiva_backend.utils.Parser;
import org.springframework.stereotype.Component;


@Component
public class EventMapper implements DTOMapper<Event, EventDTO> {
    private final ResourceMapper resourceMapper;

    public EventMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    public EventDTO toDTO(Event entity) {
        return new EventDTO(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCategory(),
                entity.getDate(),
                entity.getDescription(),
                entity.getBannerId(),
                entity.getResources() != null ? resourceMapper.toDTO(entity.getResources()) : null
        );
    }

    @Override
    public Event toEntity(EventDTO entityDTO) {
        return Parser.parse(entityDTO, Event.class);
    }
}
