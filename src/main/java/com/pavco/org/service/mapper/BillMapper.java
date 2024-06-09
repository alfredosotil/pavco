package com.pavco.org.service.mapper;

import com.pavco.org.domain.Bill;
import com.pavco.org.domain.Client;
import com.pavco.org.service.dto.BillDTO;
import com.pavco.org.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRuc")
    BillDTO toDto(Bill s);

    @Named("clientRuc")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ruc", source = "ruc")
    ClientDTO toDtoClientRuc(Client client);
}
