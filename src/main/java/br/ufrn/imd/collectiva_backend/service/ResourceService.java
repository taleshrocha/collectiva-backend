package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.ResourceMapper;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import br.ufrn.imd.collectiva_backend.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceService implements GenericService<Resource, ResourceDTO> {

    private final ResourceRepository repository;

    private final ResourceMapper mapper;

    public ResourceService(ResourceRepository repository, ResourceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Resource> getRepository() {
        return this.repository;
    }

    @Override
    public DTOMapper<Resource, ResourceDTO> getDtoMapper() {
        return this.mapper;
    }
}
