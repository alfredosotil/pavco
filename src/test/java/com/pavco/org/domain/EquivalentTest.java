package com.pavco.org.domain;

import static com.pavco.org.domain.ClientTestSamples.*;
import static com.pavco.org.domain.EquivalentTestSamples.*;
import static com.pavco.org.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquivalentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equivalent.class);
        Equivalent equivalent1 = getEquivalentSample1();
        Equivalent equivalent2 = new Equivalent();
        assertThat(equivalent1).isNotEqualTo(equivalent2);

        equivalent2.setId(equivalent1.getId());
        assertThat(equivalent1).isEqualTo(equivalent2);

        equivalent2 = getEquivalentSample2();
        assertThat(equivalent1).isNotEqualTo(equivalent2);
    }

    @Test
    void productTest() {
        Equivalent equivalent = getEquivalentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        equivalent.setProduct(productBack);
        assertThat(equivalent.getProduct()).isEqualTo(productBack);

        equivalent.product(null);
        assertThat(equivalent.getProduct()).isNull();
    }

    @Test
    void clientTest() {
        Equivalent equivalent = getEquivalentRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        equivalent.setClient(clientBack);
        assertThat(equivalent.getClient()).isEqualTo(clientBack);

        equivalent.client(null);
        assertThat(equivalent.getClient()).isNull();
    }
}
