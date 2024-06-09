package com.pavco.org.service.impl;

import com.pavco.org.domain.ProductType;
import com.pavco.org.repository.ProductTypeRepository;
import com.pavco.org.service.ProductTypeService;
import com.pavco.org.service.dto.ProductTypeDTO;
import com.pavco.org.service.mapper.ProductTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.pavco.org.domain.ProductType}.
 */
@Service
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
    }

    @Override
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        log.debug("Request to save ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }

    @Override
    public ProductTypeDTO update(ProductTypeDTO productTypeDTO) {
        log.debug("Request to update ProductType : {}", productTypeDTO);
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDto(productType);
    }

    @Override
    public Optional<ProductTypeDTO> partialUpdate(ProductTypeDTO productTypeDTO) {
        log.debug("Request to partially update ProductType : {}", productTypeDTO);

        return productTypeRepository
            .findById(productTypeDTO.getId())
            .map(existingProductType -> {
                productTypeMapper.partialUpdate(existingProductType, productTypeDTO);

                return existingProductType;
            })
            .map(productTypeRepository::save)
            .map(productTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findAll() {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll().stream().map(productTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the productTypes where Product is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findAllWhereProductIsNull() {
        log.debug("Request to get all productTypes where Product is null");
        return StreamSupport.stream(productTypeRepository.findAll().spliterator(), false)
            .filter(productType -> productType.getProduct() == null)
            .map(productTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTypeDTO> findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id).map(productTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
    }
}
