package com.pavco.org.domain;

import static com.pavco.org.domain.BillDetailTestSamples.*;
import static com.pavco.org.domain.BillTestSamples.*;
import static com.pavco.org.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bill.class);
        Bill bill1 = getBillSample1();
        Bill bill2 = new Bill();
        assertThat(bill1).isNotEqualTo(bill2);

        bill2.setId(bill1.getId());
        assertThat(bill1).isEqualTo(bill2);

        bill2 = getBillSample2();
        assertThat(bill1).isNotEqualTo(bill2);
    }

    @Test
    void billDetailTest() {
        Bill bill = getBillRandomSampleGenerator();
        BillDetail billDetailBack = getBillDetailRandomSampleGenerator();

        bill.addBillDetail(billDetailBack);
        assertThat(bill.getBillDetails()).containsOnly(billDetailBack);
        assertThat(billDetailBack.getBill()).isEqualTo(bill);

        bill.removeBillDetail(billDetailBack);
        assertThat(bill.getBillDetails()).doesNotContain(billDetailBack);
        assertThat(billDetailBack.getBill()).isNull();

        bill.billDetails(new HashSet<>(Set.of(billDetailBack)));
        assertThat(bill.getBillDetails()).containsOnly(billDetailBack);
        assertThat(billDetailBack.getBill()).isEqualTo(bill);

        bill.setBillDetails(new HashSet<>());
        assertThat(bill.getBillDetails()).doesNotContain(billDetailBack);
        assertThat(billDetailBack.getBill()).isNull();
    }

    @Test
    void clientTest() {
        Bill bill = getBillRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        bill.setClient(clientBack);
        assertThat(bill.getClient()).isEqualTo(clientBack);

        bill.client(null);
        assertThat(bill.getClient()).isNull();
    }
}
