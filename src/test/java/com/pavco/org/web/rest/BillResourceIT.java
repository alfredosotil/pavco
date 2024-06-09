package com.pavco.org.web.rest;

import static com.pavco.org.domain.BillAsserts.*;
import static com.pavco.org.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavco.org.IntegrationTest;
import com.pavco.org.domain.Bill;
import com.pavco.org.repository.BillRepository;
import com.pavco.org.service.BillService;
import com.pavco.org.service.dto.BillDTO;
import com.pavco.org.service.mapper.BillMapper;
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
 * Integration tests for the {@link BillResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BillResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillRepository billRepository;

    @Mock
    private BillRepository billRepositoryMock;

    @Autowired
    private BillMapper billMapper;

    @Mock
    private BillService billServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillMockMvc;

    private Bill bill;

    private Bill insertedBill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill().uuid(DEFAULT_UUID).code(DEFAULT_CODE).notes(DEFAULT_NOTES);
        return bill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createUpdatedEntity(EntityManager em) {
        Bill bill = new Bill().uuid(UPDATED_UUID).code(UPDATED_CODE).notes(UPDATED_NOTES);
        return bill;
    }

    @BeforeEach
    public void initTest() {
        bill = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBill != null) {
            billRepository.delete(insertedBill);
            insertedBill = null;
        }
    }

    @Test
    @Transactional
    void createBill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);
        var returnedBillDTO = om.readValue(
            restBillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillDTO.class
        );

        // Validate the Bill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBill = billMapper.toEntity(returnedBillDTO);
        assertBillUpdatableFieldsEquals(returnedBill, getPersistedBill(returnedBill));

        insertedBill = returnedBill;
    }

    @Test
    @Transactional
    void createBillWithExistingId() throws Exception {
        // Create the Bill with an existing ID
        bill.setId(1L);
        BillDTO billDTO = billMapper.toDto(bill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bill.setCode(null);

        // Create the Bill, which fails.
        BillDTO billDTO = billMapper.toDto(bill);

        restBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBills() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        // Get all the billList
        restBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillsWithEagerRelationshipsIsEnabled() throws Exception {
        when(billServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(billServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(billServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(billRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBill() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc
            .perform(get(ENTITY_API_URL_ID, bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBill() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bill
        Bill updatedBill = billRepository.findById(bill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBill are not directly saved in db
        em.detach(updatedBill);
        updatedBill.uuid(UPDATED_UUID).code(UPDATED_CODE).notes(UPDATED_NOTES);
        BillDTO billDTO = billMapper.toDto(updatedBill);

        restBillMockMvc
            .perform(put(ENTITY_API_URL_ID, billDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isOk());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillToMatchAllProperties(updatedBill);
    }

    @Test
    @Transactional
    void putNonExistingBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(put(ENTITY_API_URL_ID, billDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillWithPatch() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill.uuid(UPDATED_UUID).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBill, bill), getPersistedBill(bill));
    }

    @Test
    @Transactional
    void fullUpdateBillWithPatch() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bill using partial update
        Bill partialUpdatedBill = new Bill();
        partialUpdatedBill.setId(bill.getId());

        partialUpdatedBill.uuid(UPDATED_UUID).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBill))
            )
            .andExpect(status().isOk());

        // Validate the Bill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillUpdatableFieldsEquals(partialUpdatedBill, getPersistedBill(partialUpdatedBill));
    }

    @Test
    @Transactional
    void patchNonExistingBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bill.setId(longCount.incrementAndGet());

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBill() throws Exception {
        // Initialize the database
        insertedBill = billRepository.saveAndFlush(bill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bill
        restBillMockMvc
            .perform(delete(ENTITY_API_URL_ID, bill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billRepository.count();
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

    protected Bill getPersistedBill(Bill bill) {
        return billRepository.findById(bill.getId()).orElseThrow();
    }

    protected void assertPersistedBillToMatchAllProperties(Bill expectedBill) {
        assertBillAllPropertiesEquals(expectedBill, getPersistedBill(expectedBill));
    }

    protected void assertPersistedBillToMatchUpdatableProperties(Bill expectedBill) {
        assertBillAllUpdatablePropertiesEquals(expectedBill, getPersistedBill(expectedBill));
    }
}
