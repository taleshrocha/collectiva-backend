package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.ResourceHistoryDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.ResourceHistoryMapper;
import br.ufrn.imd.collectiva_backend.model.ResourceHistory;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import br.ufrn.imd.collectiva_backend.repository.ResourceHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResourceHistoryService implements GenericService<ResourceHistory, ResourceHistoryDTO> {

    private final ResourceHistoryRepository repository;

    private final ResourceHistoryMapper mapper;

    public ResourceHistoryService(ResourceHistoryRepository repository, ResourceHistoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<ResourceHistory> getRepository() {
        return this.repository;
    }

    @Override
    public DTOMapper<ResourceHistory, ResourceHistoryDTO> getDtoMapper() {
        return this.mapper;
    }
}
