package com.pavco.org.domain;

import static com.pavco.org.domain.BillFileTestSamples.*;
import static com.pavco.org.domain.ClientTestSamples.*;
import static com.pavco.org.domain.EquivalentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void billFileTest() {
        Client client = getClientRandomSampleGenerator();
        BillFile billFileBack = getBillFileRandomSampleGenerator();

        client.addBillFile(billFileBack);
        assertThat(client.getBillFiles()).containsOnly(billFileBack);
        assertThat(billFileBack.getClient()).isEqualTo(client);

        client.removeBillFile(billFileBack);
        assertThat(client.getBillFiles()).doesNotContain(billFileBack);
        assertThat(billFileBack.getClient()).isNull();

        client.billFiles(new HashSet<>(Set.of(billFileBack)));
        assertThat(client.getBillFiles()).containsOnly(billFileBack);
        assertThat(billFileBack.getClient()).isEqualTo(client);

        client.setBillFiles(new HashSet<>());
        assertThat(client.getBillFiles()).doesNotContain(billFileBack);
        assertThat(billFileBack.getClient()).isNull();
    }

    @Test
    void equivalentTest() {
        Client client = getClientRandomSampleGenerator();
        Equivalent equivalentBack = getEquivalentRandomSampleGenerator();

        client.setEquivalent(equivalentBack);
        assertThat(client.getEquivalent()).isEqualTo(equivalentBack);
        assertThat(equivalentBack.getClient()).isEqualTo(client);

        client.equivalent(null);
        assertThat(client.getEquivalent()).isNull();
        assertThat(equivalentBack.getClient()).isNull();
    }
}
