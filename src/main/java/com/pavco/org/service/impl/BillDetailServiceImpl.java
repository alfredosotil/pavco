package com.pavco.org.service.impl;

import com.pavco.org.domain.BillDetail;
import com.pavco.org.repository.BillDetailRepository;
import com.pavco.org.service.BillDetailService;
import com.pavco.org.service.dto.BillDetailDTO;
import com.pavco.org.service.mapper.BillDetailMapper;
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
 * Service Implementation for managing {@link com.pavco.org.domain.BillDetail}.
 */
@Service
@Transactional
public class BillDetailServiceImpl implements BillDetailService {

    private final Logger log = LoggerFactory.getLogger(BillDetailServiceImpl.class);

    private final BillDetailRepository billDetailRepository;

    private final BillDetailMapper billDetailMapper;

    public BillDetailServiceImpl(BillDetailRepository billDetailRepository, BillDetailMapper billDetailMapper) {
        this.billDetailRepository = billDetailRepository;
        this.billDetailMapper = billDetailMapper;
    }

    @Override
    public BillDetailDTO save(BillDetailDTO billDetailDTO) {
        log.debug("Request to save BillDetail : {}", billDetailDTO);
        BillDetail billDetail = billDetailMapper.toEntity(billDetailDTO);
        billDetail = billDetailRepository.save(billDetail);
        return billDetailMapper.toDto(billDetail);
    }

    @Override
    public BillDetailDTO update(BillDetailDTO billDetailDTO) {
        log.debug("Request to update BillDetail : {}", billDetailDTO);
        BillDetail billDetail = billDetailMapper.toEntity(billDetailDTO);
        billDetail.setIsPersisted();
        billDetail = billDetailRepository.save(billDetail);
        return billDetailMapper.toDto(billDetail);
    }

    @Override
    public Optional<BillDetailDTO> partialUpdate(BillDetailDTO billDetailDTO) {
        log.debug("Request to partially update BillDetail : {}", billDetailDTO);

        return billDetailRepository
            .findById(billDetailDTO.getId())
            .map(existingBillDetail -> {
                billDetailMapper.partialUpdate(existingBillDetail, billDetailDTO);

                return existingBillDetail;
            })
            .map(billDetailRepository::save)
            .map(billDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillDetailDTO> findAll() {
        log.debug("Request to get all BillDetails");
        return billDetailRepository.findAll().stream().map(billDetailMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<BillDetailDTO> findAllWithEagerRelationships(Pageable pageable) {
        return billDetailRepository.findAllWithEagerRelationships(pageable).map(billDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillDetailDTO> findOne(Long id) {
        log.debug("Request to get BillDetail : {}", id);
        return billDetailRepository.findOneWithEagerRelationships(id).map(billDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillDetail : {}", id);
        billDetailRepository.deleteById(id);
    }
}
