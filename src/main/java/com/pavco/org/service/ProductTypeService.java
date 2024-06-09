package com.pavco.org.service;

import com.pavco.org.service.dto.ProductTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pavco.org.domain.ProductType}.
 */
public interface ProductTypeService {
    /**
     * Save a productType.
     *
     * @param productTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ProductTypeDTO save(ProductTypeDTO productTypeDTO);

    /**
     * Updates a productType.
     *
     * @param productTypeDTO the entity to update.
     * @return the persisted entity.
     */
    ProductTypeDTO update(ProductTypeDTO productTypeDTO);

    /**
     * Partially updates a productType.
     *
     * @param productTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductTypeDTO> partialUpdate(ProductTypeDTO productTypeDTO);

    /**
     * Get all the productTypes.
     *
     * @return the list of entities.
     */
    List<ProductTypeDTO> findAll();

    /**
     * Get all the ProductTypeDTO where Product is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ProductTypeDTO> findAllWhereProductIsNull();

    /**
     * Get the "id" productType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductTypeDTO> findOne(Long id);

    /**
     * Delete the "id" productType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
