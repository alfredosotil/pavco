package com.pavco.org.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BillDetailAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillDetailAllPropertiesEquals(BillDetail expected, BillDetail actual) {
        assertBillDetailAutoGeneratedPropertiesEquals(expected, actual);
        assertBillDetailAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillDetailAllUpdatablePropertiesEquals(BillDetail expected, BillDetail actual) {
        assertBillDetailUpdatableFieldsEquals(expected, actual);
        assertBillDetailUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillDetailAutoGeneratedPropertiesEquals(BillDetail expected, BillDetail actual) {
        assertThat(expected)
            .as("Verify BillDetail auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillDetailUpdatableFieldsEquals(BillDetail expected, BillDetail actual) {
        assertThat(expected)
            .as("Verify BillDetail relevant properties")
            .satisfies(e -> assertThat(e.getUuid()).as("check uuid").isEqualTo(actual.getUuid()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillDetailUpdatableRelationshipsEquals(BillDetail expected, BillDetail actual) {
        assertThat(expected)
            .as("Verify BillDetail relationships")
            .satisfies(e -> assertThat(e.getBill()).as("check bill").isEqualTo(actual.getBill()));
    }
}