package com.pavco.org.web.rest;

import static com.pavco.org.domain.BillDetailAsserts.*;
import static com.pavco.org.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavco.org.IntegrationTest;
import com.pavco.org.domain.BillDetail;
import com.pavco.org.repository.BillDetailRepository;
import com.pavco.org.service.BillDetailService;
import com.pavco.org.service.dto.BillDetailDTO;
import com.pavco.org.service.mapper.BillDetailMapper;
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
 * Integration tests for the {@link BillDetailResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BillDetailResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_CODE = "1234567890";
    private static final String UPDATED_CODE = "123456789012";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/bill-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Mock
    private BillDetailRepository billDetailRepositoryMock;

    @Autowired
    private BillDetailMapper billDetailMapper;

    @Mock
    private BillDetailService billDetailServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillDetailMockMvc;

    private BillDetail billDetail;

    private BillDetail insertedBillDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillDetail createEntity(EntityManager em) {
        BillDetail billDetail = new BillDetail()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        return billDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillDetail createUpdatedEntity(EntityManager em) {
        BillDetail billDetail = new BillDetail()
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);
        return billDetail;
    }

    @BeforeEach
    public void initTest() {
        billDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBillDetail != null) {
            billDetailRepository.delete(insertedBillDetail);
            insertedBillDetail = null;
        }
    }

    @Test
    @Transactional
    void createBillDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);
        var returnedBillDetailDTO = om.readValue(
            restBillDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillDetailDTO.class
        );

        // Validate the BillDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBillDetail = billDetailMapper.toEntity(returnedBillDetailDTO);
        assertBillDetailUpdatableFieldsEquals(returnedBillDetail, getPersistedBillDetail(returnedBillDetail));

        insertedBillDetail = returnedBillDetail;
    }

    @Test
    @Transactional
    void createBillDetailWithExistingId() throws Exception {
        // Create the BillDetail with an existing ID
        billDetail.setId(1L);
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billDetail.setCode(null);

        // Create the BillDetail, which fails.
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        restBillDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billDetail.setDescription(null);

        // Create the BillDetail, which fails.
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        restBillDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billDetail.setPrice(null);

        // Create the BillDetail, which fails.
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        restBillDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billDetail.setQuantity(null);

        // Create the BillDetail, which fails.
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        restBillDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillDetails() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        // Get all the billDetailList
        restBillDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(billDetailServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(billDetailServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(billDetailServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(billDetailRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBillDetail() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        // Get the billDetail
        restBillDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, billDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billDetail.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingBillDetail() throws Exception {
        // Get the billDetail
        restBillDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillDetail() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billDetail
        BillDetail updatedBillDetail = billDetailRepository.findById(billDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillDetail are not directly saved in db
        em.detach(updatedBillDetail);
        updatedBillDetail
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(updatedBillDetail);

        restBillDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillDetailToMatchAllProperties(updatedBillDetail);
    }

    @Test
    @Transactional
    void putNonExistingBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillDetailWithPatch() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billDetail using partial update
        BillDetail partialUpdatedBillDetail = new BillDetail();
        partialUpdatedBillDetail.setId(billDetail.getId());

        partialUpdatedBillDetail.uuid(UPDATED_UUID).description(UPDATED_DESCRIPTION);

        restBillDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillDetail))
            )
            .andExpect(status().isOk());

        // Validate the BillDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBillDetail, billDetail),
            getPersistedBillDetail(billDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateBillDetailWithPatch() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billDetail using partial update
        BillDetail partialUpdatedBillDetail = new BillDetail();
        partialUpdatedBillDetail.setId(billDetail.getId());

        partialUpdatedBillDetail
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);

        restBillDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillDetail))
            )
            .andExpect(status().isOk());

        // Validate the BillDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillDetailUpdatableFieldsEquals(partialUpdatedBillDetail, getPersistedBillDetail(partialUpdatedBillDetail));
    }

    @Test
    @Transactional
    void patchNonExistingBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billDetail.setId(longCount.incrementAndGet());

        // Create the BillDetail
        BillDetailDTO billDetailDTO = billDetailMapper.toDto(billDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillDetail() throws Exception {
        // Initialize the database
        insertedBillDetail = billDetailRepository.saveAndFlush(billDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billDetail
        restBillDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, billDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billDetailRepository.count();
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

    protected BillDetail getPersistedBillDetail(BillDetail billDetail) {
        return billDetailRepository.findById(billDetail.getId()).orElseThrow();
    }

    protected void assertPersistedBillDetailToMatchAllProperties(BillDetail expectedBillDetail) {
        assertBillDetailAllPropertiesEquals(expectedBillDetail, getPersistedBillDetail(expectedBillDetail));
    }

    protected void assertPersistedBillDetailToMatchUpdatableProperties(BillDetail expectedBillDetail) {
        assertBillDetailAllUpdatablePropertiesEquals(expectedBillDetail, getPersistedBillDetail(expectedBillDetail));
    }
}
