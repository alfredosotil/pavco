package com.pavco.org.service;

import com.pavco.org.service.dto.BillDetailDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pavco.org.domain.BillDetail}.
 */
public interface BillDetailService {
    /**
     * Save a billDetail.
     *
     * @param billDetailDTO the entity to save.
     * @return the persisted entity.
     */
    BillDetailDTO save(BillDetailDTO billDetailDTO);

    /**
     * Updates a billDetail.
     *
     * @param billDetailDTO the entity to update.
     * @return the persisted entity.
     */
    BillDetailDTO update(BillDetailDTO billDetailDTO);

    /**
     * Partially updates a billDetail.
     *
     * @param billDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillDetailDTO> partialUpdate(BillDetailDTO billDetailDTO);

    /**
     * Get all the billDetails.
     *
     * @return the list of entities.
     */
    List<BillDetailDTO> findAll();

    /**
     * Get all the billDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillDetailDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" billDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillDetailDTO> findOne(Long id);

    /**
     * Delete the "id" billDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
