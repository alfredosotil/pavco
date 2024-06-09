package com.pavco.org.domain;

import static com.pavco.org.domain.BillFileTestSamples.*;
import static com.pavco.org.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillFile.class);
        BillFile billFile1 = getBillFileSample1();
        BillFile billFile2 = new BillFile();
        assertThat(billFile1).isNotEqualTo(billFile2);

        billFile2.setId(billFile1.getId());
        assertThat(billFile1).isEqualTo(billFile2);

        billFile2 = getBillFileSample2();
        assertThat(billFile1).isNotEqualTo(billFile2);
    }

    @Test
    void clientTest() {
        BillFile billFile = getBillFileRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        billFile.setClient(clientBack);
        assertThat(billFile.getClient()).isEqualTo(clientBack);

        billFile.client(null);
        assertThat(billFile.getClient()).isNull();
    }
}
