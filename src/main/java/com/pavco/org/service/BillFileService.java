package com.pavco.org.service;

import com.pavco.org.service.dto.BillFileDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pavco.org.domain.BillFile}.
 */
public interface BillFileService {
    /**
     * Save a billFile.
     *
     * @param billFileDTO the entity to save.
     * @return the persisted entity.
     */
    BillFileDTO save(BillFileDTO billFileDTO);

    /**
     * Updates a billFile.
     *
     * @param billFileDTO the entity to update.
     * @return the persisted entity.
     */
    BillFileDTO update(BillFileDTO billFileDTO);

    /**
     * Partially updates a billFile.
     *
     * @param billFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillFileDTO> partialUpdate(BillFileDTO billFileDTO);

    /**
     * Get all the billFiles.
     *
     * @return the list of entities.
     */
    List<BillFileDTO> findAll();

    /**
     * Get all the billFiles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillFileDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" billFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillFileDTO> findOne(Long id);

    /**
     * Delete the "id" billFile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
