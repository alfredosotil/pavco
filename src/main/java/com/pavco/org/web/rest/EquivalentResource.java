package com.pavco.org.web.rest;

import com.pavco.org.repository.EquivalentRepository;
import com.pavco.org.service.EquivalentService;
import com.pavco.org.service.dto.EquivalentDTO;
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
 * REST controller for managing {@link com.pavco.org.domain.Equivalent}.
 */
@RestController
@RequestMapping("/api/equivalents")
public class EquivalentResource {

    private final Logger log = LoggerFactory.getLogger(EquivalentResource.class);

    private static final String ENTITY_NAME = "equivalent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquivalentService equivalentService;

    private final EquivalentRepository equivalentRepository;

    public EquivalentResource(EquivalentService equivalentService, EquivalentRepository equivalentRepository) {
        this.equivalentService = equivalentService;
        this.equivalentRepository = equivalentRepository;
    }

    /**
     * {@code POST  /equivalents} : Create a new equivalent.
     *
     * @param equivalentDTO the equivalentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equivalentDTO, or with status {@code 400 (Bad Request)} if the equivalent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EquivalentDTO> createEquivalent(@Valid @RequestBody EquivalentDTO equivalentDTO) throws URISyntaxException {
        log.debug("REST request to save Equivalent : {}", equivalentDTO);
        if (equivalentDTO.getId() != null) {
            throw new BadRequestAlertException("A new equivalent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        equivalentDTO = equivalentService.save(equivalentDTO);
        return ResponseEntity.created(new URI("/api/equivalents/" + equivalentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, equivalentDTO.getId().toString()))
            .body(equivalentDTO);
    }

    /**
     * {@code PUT  /equivalents/:id} : Updates an existing equivalent.
     *
     * @param id the id of the equivalentDTO to save.
     * @param equivalentDTO the equivalentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equivalentDTO,
     * or with status {@code 400 (Bad Request)} if the equivalentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equivalentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EquivalentDTO> updateEquivalent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EquivalentDTO equivalentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Equivalent : {}, {}", id, equivalentDTO);
        if (equivalentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equivalentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equivalentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        equivalentDTO = equivalentService.update(equivalentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equivalentDTO.getId().toString()))
            .body(equivalentDTO);
    }

    /**
     * {@code PATCH  /equivalents/:id} : Partial updates given fields of an existing equivalent, field will ignore if it is null
     *
     * @param id the id of the equivalentDTO to save.
     * @param equivalentDTO the equivalentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equivalentDTO,
     * or with status {@code 400 (Bad Request)} if the equivalentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equivalentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equivalentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EquivalentDTO> partialUpdateEquivalent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EquivalentDTO equivalentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Equivalent partially : {}, {}", id, equivalentDTO);
        if (equivalentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equivalentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equivalentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquivalentDTO> result = equivalentService.partialUpdate(equivalentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equivalentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equivalents} : get all the equivalents.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equivalents in body.
     */
    @GetMapping("")
    public List<EquivalentDTO> getAllEquivalents(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Equivalents");
        return equivalentService.findAll();
    }

    /**
     * {@code GET  /equivalents/:id} : get the "id" equivalent.
     *
     * @param id the id of the equivalentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equivalentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquivalentDTO> getEquivalent(@PathVariable("id") Long id) {
        log.debug("REST request to get Equivalent : {}", id);
        Optional<EquivalentDTO> equivalentDTO = equivalentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equivalentDTO);
    }

    /**
     * {@code DELETE  /equivalents/:id} : delete the "id" equivalent.
     *
     * @param id the id of the equivalentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquivalent(@PathVariable("id") Long id) {
        log.debug("REST request to delete Equivalent : {}", id);
        equivalentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
