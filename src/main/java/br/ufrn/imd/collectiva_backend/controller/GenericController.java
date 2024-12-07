package br.ufrn.imd.collectiva_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.ufrn.imd.collectiva_backend.dto.ApiResponseDTO;
import br.ufrn.imd.collectiva_backend.dto.EntityDTO;
import br.ufrn.imd.collectiva_backend.model.BaseEntity;
import br.ufrn.imd.collectiva_backend.service.GenericService;
import jakarta.validation.Valid;

/**
 * A generic controller providing common CRUD operations for entities in the
 * application.
 * This abstract class defines methods for handling HTTP requests related to
 * entity management.
 *
 * @param <E>   The type of the entity extending BaseEntity.
 * @param <DTO> The type of the DTO (Data Transfer Object) associated with the
 *              entity.
 * @param <S>   The type of the service extending GenericService for the entity.
 */
@Validated
public abstract class GenericController<E extends BaseEntity, DTO extends EntityDTO, S extends GenericService<E, DTO>> {

    protected S service;

    /**
     * Constructs a GenericController instance with the provided service.
     *
     * @param service The service associated with the controller.
     */
    protected GenericController(S service) {
        this.service = service;
    }

    /**
     * Get all the entities.
     *
     * @return ResponseEntity containing the list of DTOs and status 200 (OK).
     */
    @Operation(summary = "Listar registros de entidade com paramâmetro de paginação.", description = "Retorna uma lista de qualquer entidade associada a implementação.")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageImpl<EntityDTO>>> getAll(@ParameterObject Pageable pageable) {
        var page = service.findAll(pageable);
        var res = new PageImpl<>(page.getContent().stream().map(DTO::toResponse).toList(), pageable,
                page.getTotalElements());

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entities located successfully.",
                res,
                null));
    }

    /**
     * Get an entity by its ID.
     *
     * @param id The ID of the entity to get.
     * @return ResponseEntity containing the DTO and status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entity located successfully.",
                service.findById(id).toResponse(),
                null));
    }

    /**
     * Save a new entity.
     *
     * @param dto The DTO representing the entity to save.
     * @return ResponseEntity containing the DTO and status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<EntityDTO>> create(@Valid @RequestBody DTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(
                true,
                "Success: Entity created successfully..",
                service.create(dto).toResponse(),
                null));
    }

    /**
     * Update an existing entity.
     *
     * @param id  The ID of the entity to update.
     * @param dto The DTO representing the updated entity.
     * @return ResponseEntity containing the DTO and status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EntityDTO>> update(@PathVariable Long id, @Valid @RequestBody DTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO<>(
                true,
                "Success: Entity has been successfully updated.",
                service.update(id, dto).toResponse(),
                null));
    }

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return ResponseEntity with status 200 (OK).
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseDTO<DTO>> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Success: Entity has been successfully removed.",
                null,
                null));
    }

}
