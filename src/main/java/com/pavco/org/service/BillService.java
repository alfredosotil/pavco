package com.pavco.org.service;

import com.pavco.org.service.dto.BillDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pavco.org.domain.Bill}.
 */
public interface BillService {
    /**
     * Save a bill.
     *
     * @param billDTO the entity to save.
     * @return the persisted entity.
     */
    BillDTO save(BillDTO billDTO);

    /**
     * Updates a bill.
     *
     * @param billDTO the entity to update.
     * @return the persisted entity.
     */
    BillDTO update(BillDTO billDTO);

    /**
     * Partially updates a bill.
     *
     * @param billDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillDTO> partialUpdate(BillDTO billDTO);

    /**
     * Get all the bills.
     *
     * @return the list of entities.
     */
    List<BillDTO> findAll();

    /**
     * Get all the bills with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bill.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillDTO> findOne(Long id);

    /**
     * Delete the "id" bill.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
