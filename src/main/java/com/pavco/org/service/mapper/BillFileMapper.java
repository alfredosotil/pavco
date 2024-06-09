package com.pavco.org.service.mapper;

import com.pavco.org.domain.BillFile;
import com.pavco.org.domain.Client;
import com.pavco.org.service.dto.BillFileDTO;
import com.pavco.org.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillFile} and its DTO {@link BillFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillFileMapper extends EntityMapper<BillFileDTO, BillFile> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRuc")
    BillFileDTO toDto(BillFile s);

    @Named("clientRuc")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ruc", source = "ruc")
    ClientDTO toDtoClientRuc(Client client);
}
