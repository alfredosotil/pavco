package com.pavco.org.service.mapper;

import static com.pavco.org.domain.BillDetailAsserts.*;
import static com.pavco.org.domain.BillDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillDetailMapperTest {

    private BillDetailMapper billDetailMapper;

    @BeforeEach
    void setUp() {
        billDetailMapper = new BillDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBillDetailSample1();
        var actual = billDetailMapper.toEntity(billDetailMapper.toDto(expected));
        assertBillDetailAllPropertiesEquals(expected, actual);
    }
}
