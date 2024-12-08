package br.ufrn.imd.collectiva_backend.mappers;

import br.ufrn.imd.collectiva_backend.dto.ResourceDTO;
import br.ufrn.imd.collectiva_backend.dto.ResourceHistoryDTO;
import br.ufrn.imd.collectiva_backend.model.Resource;
import br.ufrn.imd.collectiva_backend.model.ResourceHistory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ResourceHistoryMapper implements DTOMapper<ResourceHistory, ResourceHistoryDTO> {

    @Override
    public ResourceHistoryDTO toDTO(ResourceHistory entity) {
        return new ResourceHistoryDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }

    @Override
    public ResourceHistory toEntity(ResourceHistoryDTO entityDTO) {

        ResourceHistory entity = new ResourceHistory();

        entity.setId(entityDTO.id());
        entity.setDescription(entityDTO.description());
        entity.setCreatedAt(entityDTO.createdAt());

        return entity;

    }
}
