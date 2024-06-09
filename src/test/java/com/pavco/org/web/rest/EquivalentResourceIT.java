package com.pavco.org.web.rest;

import static com.pavco.org.domain.EquivalentAsserts.*;
import static com.pavco.org.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavco.org.IntegrationTest;
import com.pavco.org.domain.Equivalent;
import com.pavco.org.repository.EquivalentRepository;
import com.pavco.org.service.EquivalentService;
import com.pavco.org.service.dto.EquivalentDTO;
import com.pavco.org.service.mapper.EquivalentMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EquivalentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EquivalentResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/equivalents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EquivalentRepository equivalentRepository;

    @Mock
    private EquivalentRepository equivalentRepositoryMock;

    @Autowired
    private EquivalentMapper equivalentMapper;

    @Mock
    private EquivalentService equivalentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquivalentMockMvc;

    private Equivalent equivalent;

    private Equivalent insertedEquivalent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equivalent createEntity(EntityManager em) {
        Equivalent equivalent = new Equivalent().uuid(DEFAULT_UUID).code(DEFAULT_CODE).price(DEFAULT_PRICE).discount(DEFAULT_DISCOUNT);
        return equivalent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equivalent createUpdatedEntity(EntityManager em) {
        Equivalent equivalent = new Equivalent().uuid(UPDATED_UUID).code(UPDATED_CODE).price(UPDATED_PRICE).discount(UPDATED_DISCOUNT);
        return equivalent;
    }

    @BeforeEach
    public void initTest() {
        equivalent = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEquivalent != null) {
            equivalentRepository.delete(insertedEquivalent);
            insertedEquivalent = null;
        }
    }

    @Test
    @Transactional
    void createEquivalent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);
        var returnedEquivalentDTO = om.readValue(
            restEquivalentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EquivalentDTO.class
        );

        // Validate the Equivalent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEquivalent = equivalentMapper.toEntity(returnedEquivalentDTO);
        assertEquivalentUpdatableFieldsEquals(returnedEquivalent, getPersistedEquivalent(returnedEquivalent));

        insertedEquivalent = returnedEquivalent;
    }

    @Test
    @Transactional
    void createEquivalentWithExistingId() throws Exception {
        // Create the Equivalent with an existing ID
        equivalent.setId(1L);
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquivalentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equivalent.setCode(null);

        // Create the Equivalent, which fails.
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        restEquivalentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equivalent.setPrice(null);

        // Create the Equivalent, which fails.
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        restEquivalentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equivalent.setDiscount(null);

        // Create the Equivalent, which fails.
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        restEquivalentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquivalents() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        // Get all the equivalentList
        restEquivalentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equivalent.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEquivalentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(equivalentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEquivalentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(equivalentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEquivalentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(equivalentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEquivalentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(equivalentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEquivalent() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        // Get the equivalent
        restEquivalentMockMvc
            .perform(get(ENTITY_API_URL_ID, equivalent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equivalent.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEquivalent() throws Exception {
        // Get the equivalent
        restEquivalentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquivalent() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equivalent
        Equivalent updatedEquivalent = equivalentRepository.findById(equivalent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEquivalent are not directly saved in db
        em.detach(updatedEquivalent);
        updatedEquivalent.uuid(UPDATED_UUID).code(UPDATED_CODE).price(UPDATED_PRICE).discount(UPDATED_DISCOUNT);
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(updatedEquivalent);

        restEquivalentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equivalentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equivalentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEquivalentToMatchAllProperties(updatedEquivalent);
    }

    @Test
    @Transactional
    void putNonExistingEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equivalentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equivalentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equivalentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquivalentWithPatch() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equivalent using partial update
        Equivalent partialUpdatedEquivalent = new Equivalent();
        partialUpdatedEquivalent.setId(equivalent.getId());

        partialUpdatedEquivalent.uuid(UPDATED_UUID).discount(UPDATED_DISCOUNT);

        restEquivalentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquivalent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquivalent))
            )
            .andExpect(status().isOk());

        // Validate the Equivalent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquivalentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEquivalent, equivalent),
            getPersistedEquivalent(equivalent)
        );
    }

    @Test
    @Transactional
    void fullUpdateEquivalentWithPatch() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equivalent using partial update
        Equivalent partialUpdatedEquivalent = new Equivalent();
        partialUpdatedEquivalent.setId(equivalent.getId());

        partialUpdatedEquivalent.uuid(UPDATED_UUID).code(UPDATED_CODE).price(UPDATED_PRICE).discount(UPDATED_DISCOUNT);

        restEquivalentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquivalent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquivalent))
            )
            .andExpect(status().isOk());

        // Validate the Equivalent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquivalentUpdatableFieldsEquals(partialUpdatedEquivalent, getPersistedEquivalent(partialUpdatedEquivalent));
    }

    @Test
    @Transactional
    void patchNonExistingEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equivalentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equivalentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equivalentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquivalent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equivalent.setId(longCount.incrementAndGet());

        // Create the Equivalent
        EquivalentDTO equivalentDTO = equivalentMapper.toDto(equivalent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquivalentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(equivalentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equivalent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquivalent() throws Exception {
        // Initialize the database
        insertedEquivalent = equivalentRepository.saveAndFlush(equivalent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the equivalent
        restEquivalentMockMvc
            .perform(delete(ENTITY_API_URL_ID, equivalent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return equivalentRepository.count();
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

    protected Equivalent getPersistedEquivalent(Equivalent equivalent) {
        return equivalentRepository.findById(equivalent.getId()).orElseThrow();
    }

    protected void assertPersistedEquivalentToMatchAllProperties(Equivalent expectedEquivalent) {
        assertEquivalentAllPropertiesEquals(expectedEquivalent, getPersistedEquivalent(expectedEquivalent));
    }

    protected void assertPersistedEquivalentToMatchUpdatableProperties(Equivalent expectedEquivalent) {
        assertEquivalentAllUpdatablePropertiesEquals(expectedEquivalent, getPersistedEquivalent(expectedEquivalent));
    }
}
