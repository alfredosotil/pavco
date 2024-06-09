package com.pavco.org.web.rest;

import com.pavco.org.repository.BillFileRepository;
import com.pavco.org.service.BillFileService;
import com.pavco.org.service.dto.BillFileDTO;
import com.pavco.org.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pavco.org.domain.BillFile}.
 */
@RestController
@RequestMapping("/api/bill-files")
public class BillFileResource {

    private final Logger log = LoggerFactory.getLogger(BillFileResource.class);

    private static final String ENTITY_NAME = "billFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillFileService billFileService;

    private final BillFileRepository billFileRepository;

    public BillFileResource(BillFileService billFileService, BillFileRepository billFileRepository) {
        this.billFileService = billFileService;
        this.billFileRepository = billFileRepository;
    }

    /**
     * {@code POST  /bill-files} : Create a new billFile.
     *
     * @param billFileDTO the billFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billFileDTO, or with status {@code 400 (Bad Request)} if the billFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillFileDTO> createBillFile(@Valid @RequestBody BillFileDTO billFileDTO) throws URISyntaxException {
        log.debug("REST request to save BillFile : {}", billFileDTO);
        if (billFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new billFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billFileDTO = billFileService.save(billFileDTO);
        return ResponseEntity.created(new URI("/api/bill-files/" + billFileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billFileDTO.getId().toString()))
            .body(billFileDTO);
    }

    /**
     * {@code PUT  /bill-files/:id} : Updates an existing billFile.
     *
     * @param id the id of the billFileDTO to save.
     * @param billFileDTO the billFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billFileDTO,
     * or with status {@code 400 (Bad Request)} if the billFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillFileDTO> updateBillFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillFileDTO billFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillFile : {}, {}", id, billFileDTO);
        if (billFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billFileDTO = billFileService.update(billFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billFileDTO.getId().toString()))
            .body(billFileDTO);
    }

    /**
     * {@code PATCH  /bill-files/:id} : Partial updates given fields of an existing billFile, field will ignore if it is null
     *
     * @param id the id of the billFileDTO to save.
     * @param billFileDTO the billFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billFileDTO,
     * or with status {@code 400 (Bad Request)} if the billFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillFileDTO> partialUpdateBillFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillFileDTO billFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillFile partially : {}, {}", id, billFileDTO);
        if (billFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billFileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillFileDTO> result = billFileService.partialUpdate(billFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-files} : get all the billFiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billFiles in body.
     */
    @GetMapping("")
    public List<BillFileDTO> getAllBillFiles(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all BillFiles");
        return billFileService.findAll();
    }

    /**
     * {@code GET  /bill-files/:id} : get the "id" billFile.
     *
     * @param id the id of the billFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillFileDTO> getBillFile(@PathVariable("id") Long id) {
        log.debug("REST request to get BillFile : {}", id);
        Optional<BillFileDTO> billFileDTO = billFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billFileDTO);
    }

    /**
     * {@code DELETE  /bill-files/:id} : delete the "id" billFile.
     *
     * @param id the id of the billFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete BillFile : {}", id);
        billFileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
