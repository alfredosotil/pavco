package com.pavco.org.web.rest;

import com.pavco.org.repository.BillDetailRepository;
import com.pavco.org.service.BillDetailService;
import com.pavco.org.service.dto.BillDetailDTO;
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
 * REST controller for managing {@link com.pavco.org.domain.BillDetail}.
 */
@RestController
@RequestMapping("/api/bill-details")
public class BillDetailResource {

    private final Logger log = LoggerFactory.getLogger(BillDetailResource.class);

    private static final String ENTITY_NAME = "billDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillDetailService billDetailService;

    private final BillDetailRepository billDetailRepository;

    public BillDetailResource(BillDetailService billDetailService, BillDetailRepository billDetailRepository) {
        this.billDetailService = billDetailService;
        this.billDetailRepository = billDetailRepository;
    }

    /**
     * {@code POST  /bill-details} : Create a new billDetail.
     *
     * @param billDetailDTO the billDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billDetailDTO, or with status {@code 400 (Bad Request)} if the billDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillDetailDTO> createBillDetail(@Valid @RequestBody BillDetailDTO billDetailDTO) throws URISyntaxException {
        log.debug("REST request to save BillDetail : {}", billDetailDTO);
        if (billDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new billDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billDetailDTO = billDetailService.save(billDetailDTO);
        return ResponseEntity.created(new URI("/api/bill-details/" + billDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billDetailDTO.getId().toString()))
            .body(billDetailDTO);
    }

    /**
     * {@code PUT  /bill-details/:id} : Updates an existing billDetail.
     *
     * @param id the id of the billDetailDTO to save.
     * @param billDetailDTO the billDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billDetailDTO,
     * or with status {@code 400 (Bad Request)} if the billDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillDetailDTO> updateBillDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillDetailDTO billDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BillDetail : {}, {}", id, billDetailDTO);
        if (billDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billDetailDTO = billDetailService.update(billDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billDetailDTO.getId().toString()))
            .body(billDetailDTO);
    }

    /**
     * {@code PATCH  /bill-details/:id} : Partial updates given fields of an existing billDetail, field will ignore if it is null
     *
     * @param id the id of the billDetailDTO to save.
     * @param billDetailDTO the billDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billDetailDTO,
     * or with status {@code 400 (Bad Request)} if the billDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillDetailDTO> partialUpdateBillDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillDetailDTO billDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillDetail partially : {}, {}", id, billDetailDTO);
        if (billDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillDetailDTO> result = billDetailService.partialUpdate(billDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-details} : get all the billDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billDetails in body.
     */
    @GetMapping("")
    public List<BillDetailDTO> getAllBillDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all BillDetails");
        return billDetailService.findAll();
    }

    /**
     * {@code GET  /bill-details/:id} : get the "id" billDetail.
     *
     * @param id the id of the billDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillDetailDTO> getBillDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get BillDetail : {}", id);
        Optional<BillDetailDTO> billDetailDTO = billDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billDetailDTO);
    }

    /**
     * {@code DELETE  /bill-details/:id} : delete the "id" billDetail.
     *
     * @param id the id of the billDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete BillDetail : {}", id);
        billDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
