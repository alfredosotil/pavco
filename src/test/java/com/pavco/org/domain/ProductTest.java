package com.pavco.org.domain;

import static com.pavco.org.domain.EquivalentTestSamples.*;
import static com.pavco.org.domain.ProductTestSamples.*;
import static com.pavco.org.domain.ProductTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void productTypeTest() {
        Product product = getProductRandomSampleGenerator();
        ProductType productTypeBack = getProductTypeRandomSampleGenerator();

        product.setProductType(productTypeBack);
        assertThat(product.getProductType()).isEqualTo(productTypeBack);

        product.productType(null);
        assertThat(product.getProductType()).isNull();
    }

    @Test
    void equivalentTest() {
        Product product = getProductRandomSampleGenerator();
        Equivalent equivalentBack = getEquivalentRandomSampleGenerator();

        product.setEquivalent(equivalentBack);
        assertThat(product.getEquivalent()).isEqualTo(equivalentBack);
        assertThat(equivalentBack.getProduct()).isEqualTo(product);

        product.equivalent(null);
        assertThat(product.getEquivalent()).isNull();
        assertThat(equivalentBack.getProduct()).isNull();
    }
}
