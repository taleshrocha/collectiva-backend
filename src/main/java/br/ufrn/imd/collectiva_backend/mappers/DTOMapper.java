package br.ufrn.imd.collectiva_backend.mappers;

import java.util.List;

/**
 * Interface defining mapping operations between entity objects (E) and DTOs
 * (DTO).
 * Provides methods to convert entities to DTOs and vice versa.
 *
 * @param <E>   The type of the entity.
 * @param <DTO> The type of the DTO.
 */
public interface DTOMapper<E, DTO> {

    /**
     * Converts an entity to its corresponding DTO.
     *
     * @param entity The entity to be converted.
     * @return The DTO representing the entity.
     */
    DTO toDTO(E entity);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entity The list of entities to be converted.
     * @return The list of DTOs representing the entities.
     */
    default List<DTO> toDTO(List<E> entity) {
        return entity.stream().map(this::toDTO).toList();
    }

    /**
     * Converts a DTO to its corresponding entity.
     *
     * @param dto The DTO to be converted.
     * @return The entity representing the DTO.
     */
    E toEntity(DTO dto);

}
