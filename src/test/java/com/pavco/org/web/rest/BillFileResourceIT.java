package com.pavco.org.web.rest;

import static com.pavco.org.domain.BillFileAsserts.*;
import static com.pavco.org.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavco.org.IntegrationTest;
import com.pavco.org.domain.BillFile;
import com.pavco.org.repository.BillFileRepository;
import com.pavco.org.service.BillFileService;
import com.pavco.org.service.dto.BillFileDTO;
import com.pavco.org.service.mapper.BillFileMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link BillFileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BillFileResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_PROCESSED = false;
    private static final Boolean UPDATED_IS_PROCESSED = true;

    private static final String ENTITY_API_URL = "/api/bill-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillFileRepository billFileRepository;

    @Mock
    private BillFileRepository billFileRepositoryMock;

    @Autowired
    private BillFileMapper billFileMapper;

    @Mock
    private BillFileService billFileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillFileMockMvc;

    private BillFile billFile;

    private BillFile insertedBillFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillFile createEntity(EntityManager em) {
        BillFile billFile = new BillFile()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .size(DEFAULT_SIZE)
            .mimeType(DEFAULT_MIME_TYPE)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .isProcessed(DEFAULT_IS_PROCESSED);
        return billFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillFile createUpdatedEntity(EntityManager em) {
        BillFile billFile = new BillFile()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .size(UPDATED_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .isProcessed(UPDATED_IS_PROCESSED);
        return billFile;
    }

    @BeforeEach
    public void initTest() {
        billFile = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBillFile != null) {
            billFileRepository.delete(insertedBillFile);
            insertedBillFile = null;
        }
    }

    @Test
    @Transactional
    void createBillFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);
        var returnedBillFileDTO = om.readValue(
            restBillFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillFileDTO.class
        );

        // Validate the BillFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBillFile = billFileMapper.toEntity(returnedBillFileDTO);
        assertBillFileUpdatableFieldsEquals(returnedBillFile, getPersistedBillFile(returnedBillFile));

        insertedBillFile = returnedBillFile;
    }

    @Test
    @Transactional
    void createBillFileWithExistingId() throws Exception {
        // Create the BillFile with an existing ID
        billFile.setId(1L);
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billFile.setName(null);

        // Create the BillFile, which fails.
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        restBillFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billFile.setSize(null);

        // Create the BillFile, which fails.
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        restBillFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMimeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billFile.setMimeType(null);

        // Create the BillFile, which fails.
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        restBillFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillFiles() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        // Get all the billFileList
        restBillFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].isProcessed").value(hasItem(DEFAULT_IS_PROCESSED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillFilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(billFileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(billFileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillFilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(billFileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(billFileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBillFile() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        // Get the billFile
        restBillFileMockMvc
            .perform(get(ENTITY_API_URL_ID, billFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billFile.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.isProcessed").value(DEFAULT_IS_PROCESSED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBillFile() throws Exception {
        // Get the billFile
        restBillFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillFile() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billFile
        BillFile updatedBillFile = billFileRepository.findById(billFile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillFile are not directly saved in db
        em.detach(updatedBillFile);
        updatedBillFile
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .size(UPDATED_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .isProcessed(UPDATED_IS_PROCESSED);
        BillFileDTO billFileDTO = billFileMapper.toDto(updatedBillFile);

        restBillFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillFileToMatchAllProperties(updatedBillFile);
    }

    @Test
    @Transactional
    void putNonExistingBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillFileWithPatch() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billFile using partial update
        BillFile partialUpdatedBillFile = new BillFile();
        partialUpdatedBillFile.setId(billFile.getId());

        partialUpdatedBillFile
            .name(UPDATED_NAME)
            .size(UPDATED_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .isProcessed(UPDATED_IS_PROCESSED);

        restBillFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillFile))
            )
            .andExpect(status().isOk());

        // Validate the BillFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBillFile, billFile), getPersistedBillFile(billFile));
    }

    @Test
    @Transactional
    void fullUpdateBillFileWithPatch() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billFile using partial update
        BillFile partialUpdatedBillFile = new BillFile();
        partialUpdatedBillFile.setId(billFile.getId());

        partialUpdatedBillFile
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .size(UPDATED_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .isProcessed(UPDATED_IS_PROCESSED);

        restBillFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillFile))
            )
            .andExpect(status().isOk());

        // Validate the BillFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillFileUpdatableFieldsEquals(partialUpdatedBillFile, getPersistedBillFile(partialUpdatedBillFile));
    }

    @Test
    @Transactional
    void patchNonExistingBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billFile.setId(longCount.incrementAndGet());

        // Create the BillFile
        BillFileDTO billFileDTO = billFileMapper.toDto(billFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillFile() throws Exception {
        // Initialize the database
        insertedBillFile = billFileRepository.saveAndFlush(billFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billFile
        restBillFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, billFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billFileRepository.count();
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

    protected BillFile getPersistedBillFile(BillFile billFile) {
        return billFileRepository.findById(billFile.getId()).orElseThrow();
    }

    protected void assertPersistedBillFileToMatchAllProperties(BillFile expectedBillFile) {
        assertBillFileAllPropertiesEquals(expectedBillFile, getPersistedBillFile(expectedBillFile));
    }

    protected void assertPersistedBillFileToMatchUpdatableProperties(BillFile expectedBillFile) {
        assertBillFileAllUpdatablePropertiesEquals(expectedBillFile, getPersistedBillFile(expectedBillFile));
    }
}
