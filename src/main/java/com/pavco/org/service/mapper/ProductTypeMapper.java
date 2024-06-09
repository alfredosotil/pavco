package com.pavco.org.service.mapper;

import com.pavco.org.domain.ProductType;
import com.pavco.org.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductType} and its DTO {@link ProductTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {}
