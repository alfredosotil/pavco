package com.pavco.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquivalentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquivalentDTO.class);
        EquivalentDTO equivalentDTO1 = new EquivalentDTO();
        equivalentDTO1.setId(1L);
        EquivalentDTO equivalentDTO2 = new EquivalentDTO();
        assertThat(equivalentDTO1).isNotEqualTo(equivalentDTO2);
        equivalentDTO2.setId(equivalentDTO1.getId());
        assertThat(equivalentDTO1).isEqualTo(equivalentDTO2);
        equivalentDTO2.setId(2L);
        assertThat(equivalentDTO1).isNotEqualTo(equivalentDTO2);
        equivalentDTO1.setId(null);
        assertThat(equivalentDTO1).isNotEqualTo(equivalentDTO2);
    }
}
