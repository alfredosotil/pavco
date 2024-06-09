package com.pavco.org.service.mapper;

import com.pavco.org.domain.Client;
import com.pavco.org.domain.Equivalent;
import com.pavco.org.domain.Product;
import com.pavco.org.service.dto.ClientDTO;
import com.pavco.org.service.dto.EquivalentDTO;
import com.pavco.org.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equivalent} and its DTO {@link EquivalentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EquivalentMapper extends EntityMapper<EquivalentDTO, Equivalent> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productCode")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRuc")
    EquivalentDTO toDto(Equivalent s);

    @Named("productCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    ProductDTO toDtoProductCode(Product product);

    @Named("clientRuc")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ruc", source = "ruc")
    ClientDTO toDtoClientRuc(Client client);
}
