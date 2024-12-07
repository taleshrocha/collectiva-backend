package br.ufrn.imd.collectiva_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import br.ufrn.imd.collectiva_backend.model.BaseEntity;
import jakarta.transaction.Transactional;

/**
 * A generic repository interface defining common CRUD operations for entities
 * in the application.
 *
 * @param <T> The type of the entity extending BaseEntity.
 */
@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    /**
     * Overrides the default deleteById method to mark the entity as deleted instead
     * of physically removing it.
     *
     * @param id The ID of the entity to be marked as inactive.
     */
    @Override
    @Transactional
    default void deleteById(Long id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            entity.get().setActive(false);
            save(entity.get());
        }
    }

    /**
     * Overrides the default delete method to mark the entity as deleted instead of
     * physically removing it.
     *
     * @param obj The entity to be marked as inactive.
     */
    @Override
    @Transactional
    default void delete(T obj) {
        obj.setActive(false);
        save(obj);
    }

    /**
     * Overrides the default deleteAll method to mark the entities as deleted
     * instead of physically removing them.
     *
     * @param arg0 Iterable of entities to be marked as inactive.
     */
    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> arg0) {
        arg0.forEach(entity -> deleteById(entity.getId()));
    }

    /**
     * Retrieves a list of active entities.
     *
     * @return A list of active entities.
     */
    @Query("select e from #{#entityName} e where e.active = true")
    List<T> findAll();

    /**
     * Retrieves a paginated list of active entities.
     *
     * @param pageable An object containing pagination information such as page size
     *                 and page number.
     * @return A page of active entities based on the provided pagination criteria.
     */
    @Query("select e from #{#entityName} e where e.active = true")
    Page<T> findAllPage(Pageable pageable);

    /**
     * Retrieves an active entity by its unique identifier.
     *
     * @param id The unique identifier of the entity to retrieve.
     * @return An Optional containing the active entity with the specified
     *         identifier if found, or an empty Optional if not found.
     */
    @Query("select e from #{#entityName} e where e.id = ?1 and e.active = true")
    Optional<T> findById(Long id);

}
