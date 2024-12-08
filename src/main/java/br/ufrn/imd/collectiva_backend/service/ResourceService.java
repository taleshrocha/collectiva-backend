package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.ResourceMapper;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import br.ufrn.imd.collectiva_backend.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ResourceService implements GenericService<Resource, ResourceDTO> {

    private final ResourceRepository repository;

    private final ResourceMapper mapper;

    private final EventService eventService;

    public ResourceService(ResourceRepository repository, ResourceMapper mapper, EventService eventService) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventService = eventService;
    }

    @Override
    public GenericRepository<Resource> getRepository() {
        return this.repository;
    }

    @Override
    public DTOMapper<Resource, ResourceDTO> getDtoMapper() {
        return this.mapper;
    }

//    @Override
//    public ResourceDTO create(ResourceDTO resourceDTO) {
//        Resource entity = getDtoMapper().toEntity(resourceDTO);
//        validateBeforeSave(entity);
//
//        if (resourceDTO.event() != null) {
//            eventService.addResouceByEventId(entity.getEvent().getId(), entity);
//        }
//
//        return getDtoMapper().toDTO(getRepository().save(entity));
//    }
}
