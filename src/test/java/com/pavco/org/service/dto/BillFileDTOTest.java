package com.pavco.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillFileDTO.class);
        BillFileDTO billFileDTO1 = new BillFileDTO();
        billFileDTO1.setId(1L);
        BillFileDTO billFileDTO2 = new BillFileDTO();
        assertThat(billFileDTO1).isNotEqualTo(billFileDTO2);
        billFileDTO2.setId(billFileDTO1.getId());
        assertThat(billFileDTO1).isEqualTo(billFileDTO2);
        billFileDTO2.setId(2L);
        assertThat(billFileDTO1).isNotEqualTo(billFileDTO2);
        billFileDTO1.setId(null);
        assertThat(billFileDTO1).isNotEqualTo(billFileDTO2);
    }
}
