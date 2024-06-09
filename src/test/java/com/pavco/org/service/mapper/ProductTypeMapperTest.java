package com.pavco.org.service.mapper;

import static com.pavco.org.domain.ProductTypeAsserts.*;
import static com.pavco.org.domain.ProductTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTypeMapperTest {

    private ProductTypeMapper productTypeMapper;

    @BeforeEach
    void setUp() {
        productTypeMapper = new ProductTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductTypeSample1();
        var actual = productTypeMapper.toEntity(productTypeMapper.toDto(expected));
        assertProductTypeAllPropertiesEquals(expected, actual);
    }
}
