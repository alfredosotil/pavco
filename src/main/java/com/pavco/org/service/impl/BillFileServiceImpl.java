package com.pavco.org.service.impl;

import com.pavco.org.domain.BillFile;
import com.pavco.org.repository.BillFileRepository;
import com.pavco.org.service.BillFileService;
import com.pavco.org.service.dto.BillFileDTO;
import com.pavco.org.service.mapper.BillFileMapper;
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
 * Service Implementation for managing {@link com.pavco.org.domain.BillFile}.
 */
@Service
@Transactional
public class BillFileServiceImpl implements BillFileService {

    private final Logger log = LoggerFactory.getLogger(BillFileServiceImpl.class);

    private final BillFileRepository billFileRepository;

    private final BillFileMapper billFileMapper;

    public BillFileServiceImpl(BillFileRepository billFileRepository, BillFileMapper billFileMapper) {
        this.billFileRepository = billFileRepository;
        this.billFileMapper = billFileMapper;
    }

    @Override
    public BillFileDTO save(BillFileDTO billFileDTO) {
        log.debug("Request to save BillFile : {}", billFileDTO);
        BillFile billFile = billFileMapper.toEntity(billFileDTO);
        billFile = billFileRepository.save(billFile);
        return billFileMapper.toDto(billFile);
    }

    @Override
    public BillFileDTO update(BillFileDTO billFileDTO) {
        log.debug("Request to update BillFile : {}", billFileDTO);
        BillFile billFile = billFileMapper.toEntity(billFileDTO);
        billFile.setIsPersisted();
        billFile = billFileRepository.save(billFile);
        return billFileMapper.toDto(billFile);
    }

    @Override
    public Optional<BillFileDTO> partialUpdate(BillFileDTO billFileDTO) {
        log.debug("Request to partially update BillFile : {}", billFileDTO);

        return billFileRepository
            .findById(billFileDTO.getId())
            .map(existingBillFile -> {
                billFileMapper.partialUpdate(existingBillFile, billFileDTO);

                return existingBillFile;
            })
            .map(billFileRepository::save)
            .map(billFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillFileDTO> findAll() {
        log.debug("Request to get all BillFiles");
        return billFileRepository.findAll().stream().map(billFileMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<BillFileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return billFileRepository.findAllWithEagerRelationships(pageable).map(billFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillFileDTO> findOne(Long id) {
        log.debug("Request to get BillFile : {}", id);
        return billFileRepository.findOneWithEagerRelationships(id).map(billFileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillFile : {}", id);
        billFileRepository.deleteById(id);
    }
}
