package com.pavco.org.web.rest;

import static com.pavco.org.domain.ProductTypeAsserts.*;
import static com.pavco.org.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavco.org.IntegrationTest;
import com.pavco.org.domain.ProductType;
import com.pavco.org.repository.ProductTypeRepository;
import com.pavco.org.service.dto.ProductTypeDTO;
import com.pavco.org.service.mapper.ProductTypeMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTypeResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_CODE = "1234567890";
    private static final String UPDATED_CODE = "123456789012";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/product-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTypeMockMvc;

    private ProductType productType;

    private ProductType insertedProductType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createEntity(EntityManager em) {
        ProductType productType = new ProductType()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .discount(DEFAULT_DISCOUNT);
        return productType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createUpdatedEntity(EntityManager em) {
        ProductType productType = new ProductType()
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT);
        return productType;
    }

    @BeforeEach
    public void initTest() {
        productType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductType != null) {
            productTypeRepository.delete(insertedProductType);
            insertedProductType = null;
        }
    }

    @Test
    @Transactional
    void createProductType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);
        var returnedProductTypeDTO = om.readValue(
            restProductTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductTypeDTO.class
        );

        // Validate the ProductType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductType = productTypeMapper.toEntity(returnedProductTypeDTO);
        assertProductTypeUpdatableFieldsEquals(returnedProductType, getPersistedProductType(returnedProductType));

        insertedProductType = returnedProductType;
    }

    @Test
    @Transactional
    void createProductTypeWithExistingId() throws Exception {
        // Create the ProductType with an existing ID
        productType.setId(1L);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productType.setCode(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productType.setName(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productType.setPrice(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productType.setDiscount(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductTypes() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        // Get the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productType.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductType() throws Exception {
        // Get the productType
        restProductTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType
        ProductType updatedProductType = productTypeRepository.findById(productType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductType are not directly saved in db
        em.detach(updatedProductType);
        updatedProductType.uuid(UPDATED_UUID).code(UPDATED_CODE).name(UPDATED_NAME).price(UPDATED_PRICE).discount(UPDATED_DISCOUNT);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(updatedProductType);

        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductTypeToMatchAllProperties(updatedProductType);
    }

    @Test
    @Transactional
    void putNonExistingProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.name(UPDATED_NAME);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductType, productType),
            getPersistedProductType(productType)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.uuid(UPDATED_UUID).code(UPDATED_CODE).name(UPDATED_NAME).price(UPDATED_PRICE).discount(UPDATED_DISCOUNT);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductTypeUpdatableFieldsEquals(partialUpdatedProductType, getPersistedProductType(partialUpdatedProductType));
    }

    @Test
    @Transactional
    void patchNonExistingProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productType.setId(longCount.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductType() throws Exception {
        // Initialize the database
        insertedProductType = productTypeRepository.saveAndFlush(productType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productType
        restProductTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductType getPersistedProductType(ProductType productType) {
        return productTypeRepository.findById(productType.getId()).orElseThrow();
    }

    protected void assertPersistedProductTypeToMatchAllProperties(ProductType expectedProductType) {
        assertProductTypeAllPropertiesEquals(expectedProductType, getPersistedProductType(expectedProductType));
    }

    protected void assertPersistedProductTypeToMatchUpdatableProperties(ProductType expectedProductType) {
        assertProductTypeAllUpdatablePropertiesEquals(expectedProductType, getPersistedProductType(expectedProductType));
    }
}
