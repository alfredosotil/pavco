package com.pavco.org.service.mapper;

import com.pavco.org.domain.Product;
import com.pavco.org.domain.ProductType;
import com.pavco.org.service.dto.ProductDTO;
import com.pavco.org.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "productType", source = "productType", qualifiedByName = "productTypeCode")
    ProductDTO toDto(Product s);

    @Named("productTypeCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    ProductTypeDTO toDtoProductTypeCode(ProductType productType);
}
