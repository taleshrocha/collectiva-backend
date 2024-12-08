package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.EventDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.EventMapper;
import br.ufrn.imd.collectiva_backend.model.Event;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.model.ResourceHistory;
import br.ufrn.imd.collectiva_backend.repository.EventRepository;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class EventService implements GenericService<Event, EventDTO> {

    private final EventRepository repository;

    private final EventMapper mapper;

    private final ResourceService resourceService;

    public EventService(EventRepository repository, EventMapper mapper, @Lazy ResourceService resourceService) {
        this.repository = repository;
        this.mapper = mapper;
        this.resourceService = resourceService;
    }

    @Override
    public GenericRepository<Event> getRepository() {
        return this.repository;
    }

    @Override
    public DTOMapper<Event, EventDTO> getDtoMapper() {
        return this.mapper;
    }

    public Page<EventDTO> filter(String queryEvent, String name, String location, String category, LocalDate startDate, LocalDate endDate, String description, Pageable pageable) {

        return repository.filterEventsByParams(queryEvent, name, location, category, startDate != null ? startDate.atTime(0, 0) : null, endDate != null ? endDate.atTime(23, 59) : null, description, pageable).map(mapper::toDTO);
    }

    public void addResouceByEventId(Long id, Resource resource) {
        Event event = findEntityById(id);

        event.getResources().add(resource);

        getRepository().save(event);
    }

    @Override
    public void deleteById(Long id) {
        Event entity = findEntityById(id);

        freeResources(entity);

        entity.setActive(false);
        getRepository().save(entity);
    }

    public EventDTO finishById(Long id) {
        Event entity = findEntityById(id);

        freeResources(entity);

        entity.setResources(Collections.emptyList());

        entity.setIsFinished(true);
        return mapper.toDTO(getRepository().save(entity));
    }

    private void freeResources(Event entity) {
        if (entity.getResources() != null && !entity.getResources().isEmpty()) {

            List<Resource> eventResources = entity.getResources().stream().peek((resource -> {
                ResourceHistory resourceHistory = new ResourceHistory();
                resourceHistory.setCreatedAt(ZonedDateTime.now());
                resourceHistory.setDescription("Removido do evento: " + entity.getName() + ".");
                resource.getResourceHistory().add(resourceHistory);
                resource.setEvent(null);
            })).toList();

            resourceService.saveAll(eventResources);
        }
    }

    private void allocateResources(Event entity) {
        if (entity.getResources() != null && !entity.getResources().isEmpty()) {

            List<Resource> eventResources = entity.getResources().stream().map((resource -> {
                resource = resourceService.findEntityById(resource.getId());

                ResourceHistory newResourceHistory = new ResourceHistory();
                newResourceHistory.setCreatedAt(ZonedDateTime.now());
                newResourceHistory.setDescription("Alocado para o evento: " + entity.getName() + ".");

                resource.getResourceHistory().add(newResourceHistory);

                return resource;
            })).toList();

            resourceService.saveAll(eventResources);
        }
    }

    @Override
    public EventDTO create(EventDTO eventDTO) {
        Event entity = getDtoMapper().toEntity(eventDTO);
        validateBeforeSave(entity);

        allocateResources(entity);

        return getDtoMapper().toDTO(getRepository().save(entity));
    }
}
