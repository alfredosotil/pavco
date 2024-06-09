package com.pavco.org.domain;

import static com.pavco.org.domain.ProductTestSamples.*;
import static com.pavco.org.domain.ProductTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pavco.org.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductType.class);
        ProductType productType1 = getProductTypeSample1();
        ProductType productType2 = new ProductType();
        assertThat(productType1).isNotEqualTo(productType2);

        productType2.setId(productType1.getId());
        assertThat(productType1).isEqualTo(productType2);

        productType2 = getProductTypeSample2();
        assertThat(productType1).isNotEqualTo(productType2);
    }

    @Test
    void productTest() {
        ProductType productType = getProductTypeRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productType.setProduct(productBack);
        assertThat(productType.getProduct()).isEqualTo(productBack);
        assertThat(productBack.getProductType()).isEqualTo(productType);

        productType.product(null);
        assertThat(productType.getProduct()).isNull();
        assertThat(productBack.getProductType()).isNull();
    }
}
