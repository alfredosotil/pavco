package com.pavco.org.service;

import com.pavco.org.service.dto.EquivalentDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pavco.org.domain.Equivalent}.
 */
public interface EquivalentService {
    /**
     * Save a equivalent.
     *
     * @param equivalentDTO the entity to save.
     * @return the persisted entity.
     */
    EquivalentDTO save(EquivalentDTO equivalentDTO);

    /**
     * Updates a equivalent.
     *
     * @param equivalentDTO the entity to update.
     * @return the persisted entity.
     */
    EquivalentDTO update(EquivalentDTO equivalentDTO);

    /**
     * Partially updates a equivalent.
     *
     * @param equivalentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquivalentDTO> partialUpdate(EquivalentDTO equivalentDTO);

    /**
     * Get all the equivalents.
     *
     * @return the list of entities.
     */
    List<EquivalentDTO> findAll();

    /**
     * Get all the equivalents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EquivalentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" equivalent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquivalentDTO> findOne(Long id);

    /**
     * Delete the "id" equivalent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
