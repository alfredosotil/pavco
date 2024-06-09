package com.pavco.org.service.impl;

import com.pavco.org.domain.Equivalent;
import com.pavco.org.repository.EquivalentRepository;
import com.pavco.org.service.EquivalentService;
import com.pavco.org.service.dto.EquivalentDTO;
import com.pavco.org.service.mapper.EquivalentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.pavco.org.domain.Equivalent}.
 */
@Service
@Transactional
public class EquivalentServiceImpl implements EquivalentService {

    private final Logger log = LoggerFactory.getLogger(EquivalentServiceImpl.class);

    private final EquivalentRepository equivalentRepository;

    private final EquivalentMapper equivalentMapper;

    public EquivalentServiceImpl(EquivalentRepository equivalentRepository, EquivalentMapper equivalentMapper) {
        this.equivalentRepository = equivalentRepository;
        this.equivalentMapper = equivalentMapper;
    }

    @Override
    public EquivalentDTO save(EquivalentDTO equivalentDTO) {
        log.debug("Request to save Equivalent : {}", equivalentDTO);
        Equivalent equivalent = equivalentMapper.toEntity(equivalentDTO);
        equivalent = equivalentRepository.save(equivalent);
        return equivalentMapper.toDto(equivalent);
    }

    @Override
    public EquivalentDTO update(EquivalentDTO equivalentDTO) {
        log.debug("Request to update Equivalent : {}", equivalentDTO);
        Equivalent equivalent = equivalentMapper.toEntity(equivalentDTO);
        equivalent = equivalentRepository.save(equivalent);
        return equivalentMapper.toDto(equivalent);
    }

    @Override
    public Optional<EquivalentDTO> partialUpdate(EquivalentDTO equivalentDTO) {
        log.debug("Request to partially update Equivalent : {}", equivalentDTO);

        return equivalentRepository
            .findById(equivalentDTO.getId())
            .map(existingEquivalent -> {
                equivalentMapper.partialUpdate(existingEquivalent, equivalentDTO);

                return existingEquivalent;
            })
            .map(equivalentRepository::save)
            .map(equivalentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquivalentDTO> findAll() {
        log.debug("Request to get all Equivalents");
        return equivalentRepository.findAll().stream().map(equivalentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<EquivalentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return equivalentRepository.findAllWithEagerRelationships(pageable).map(equivalentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquivalentDTO> findOne(Long id) {
        log.debug("Request to get Equivalent : {}", id);
        return equivalentRepository.findOneWithEagerRelationships(id).map(equivalentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equivalent : {}", id);
        equivalentRepository.deleteById(id);
    }
}
