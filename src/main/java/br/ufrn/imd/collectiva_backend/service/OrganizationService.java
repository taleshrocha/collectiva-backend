package br.ufrn.imd.collectiva_backend.service;

import br.ufrn.imd.collectiva_backend.dto.OrganizationDTO;
import br.ufrn.imd.collectiva_backend.mappers.DTOMapper;
import br.ufrn.imd.collectiva_backend.mappers.OrganizationMapper;
import br.ufrn.imd.collectiva_backend.model.Organization;
import br.ufrn.imd.collectiva_backend.repository.GenericRepository;
import br.ufrn.imd.collectiva_backend.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrganizationService implements GenericService<Organization, OrganizationDTO> {

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper mapper;

    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper mapper) {
        this.organizationRepository = organizationRepository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Organization> getRepository() {
        return this.organizationRepository;
    }

    @Override
    public DTOMapper<Organization, OrganizationDTO> getDtoMapper() {
        return this.mapper;
    }
}
