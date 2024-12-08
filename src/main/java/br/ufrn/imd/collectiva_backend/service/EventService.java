package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.EventDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.EventMapper;
import br.ufrn.imd.collectiva_backend.model.Event;
import br.ufrn.imd.collectiva_backend.repository.EventRepository;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class EventService implements GenericService<Event, EventDTO> {

    private final EventRepository repository;

    private final EventMapper mapper;

    public EventService(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Event> getRepository() {
        return this.repository;
    }

    @Override
    public DTOMapper<Event, EventDTO> getDtoMapper() {
        return this.mapper;
    }

    public Page<EventDTO> filter(String queryEvent, String name, String location, String category,
                                 LocalDate startDate, LocalDate endDate, String description, Pageable pageable) {

        return repository.filterEventsByParams(queryEvent, name, location, category,
                startDate != null ? startDate.atTime(0, 0) : null,
                endDate != null ? endDate.atTime(23, 59) : null, description, pageable).map(mapper::toDTO);
    }
}
