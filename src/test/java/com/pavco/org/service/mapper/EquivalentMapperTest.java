package com.pavco.org.service.mapper;

import static com.pavco.org.domain.EquivalentAsserts.*;
import static com.pavco.org.domain.EquivalentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquivalentMapperTest {

    private EquivalentMapper equivalentMapper;

    @BeforeEach
    void setUp() {
        equivalentMapper = new EquivalentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEquivalentSample1();
        var actual = equivalentMapper.toEntity(equivalentMapper.toDto(expected));
        assertEquivalentAllPropertiesEquals(expected, actual);
    }
}
