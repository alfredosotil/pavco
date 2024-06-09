package com.pavco.org.service.mapper;

import com.pavco.org.domain.Bill;
import com.pavco.org.domain.BillDetail;
import com.pavco.org.service.dto.BillDTO;
import com.pavco.org.service.dto.BillDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillDetail} and its DTO {@link BillDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillDetailMapper extends EntityMapper<BillDetailDTO, BillDetail> {
    @Mapping(target = "bill", source = "bill", qualifiedByName = "billCode")
    BillDetailDTO toDto(BillDetail s);

    @Named("billCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    BillDTO toDtoBillCode(Bill bill);
}
