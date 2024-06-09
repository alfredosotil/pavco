package com.pavco.org.service.mapper;

import static com.pavco.org.domain.BillFileAsserts.*;
import static com.pavco.org.domain.BillFileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillFileMapperTest {

    private BillFileMapper billFileMapper;

    @BeforeEach
    void setUp() {
        billFileMapper = new BillFileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBillFileSample1();
        var actual = billFileMapper.toEntity(billFileMapper.toDto(expected));
        assertBillFileAllPropertiesEquals(expected, actual);
    }
}
