package com.pavco.org.domain;

import static com.pavco.org.domain.BillDetailTestSamples.*;
import static com.pavco.org.domain.BillTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillDetail.class);
        BillDetail billDetail1 = getBillDetailSample1();
        BillDetail billDetail2 = new BillDetail();
        assertThat(billDetail1).isNotEqualTo(billDetail2);

        billDetail2.setId(billDetail1.getId());
        assertThat(billDetail1).isEqualTo(billDetail2);

        billDetail2 = getBillDetailSample2();
        assertThat(billDetail1).isNotEqualTo(billDetail2);
    }

    @Test
    void billTest() {
        BillDetail billDetail = getBillDetailRandomSampleGenerator();
        Bill billBack = getBillRandomSampleGenerator();

        billDetail.setBill(billBack);
        assertThat(billDetail.getBill()).isEqualTo(billBack);

        billDetail.bill(null);
        assertThat(billDetail.getBill()).isNull();
    }
}
