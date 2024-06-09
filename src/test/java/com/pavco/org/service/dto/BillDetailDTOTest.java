package com.pavco.org.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillDetailDTO.class);
        BillDetailDTO billDetailDTO1 = new BillDetailDTO();
        billDetailDTO1.setId(1L);
        BillDetailDTO billDetailDTO2 = new BillDetailDTO();
        assertThat(billDetailDTO1).isNotEqualTo(billDetailDTO2);
        billDetailDTO2.setId(billDetailDTO1.getId());
        assertThat(billDetailDTO1).isEqualTo(billDetailDTO2);
        billDetailDTO2.setId(2L);
        assertThat(billDetailDTO1).isNotEqualTo(billDetailDTO2);
        billDetailDTO1.setId(null);
        assertThat(billDetailDTO1).isNotEqualTo(billDetailDTO2);
    }
}
