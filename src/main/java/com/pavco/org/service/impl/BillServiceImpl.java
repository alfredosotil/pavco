package com.pavco.org.service.impl;

import com.pavco.org.domain.Bill;
import com.pavco.org.repository.BillRepository;
import com.pavco.org.service.BillService;
import com.pavco.org.service.dto.BillDTO;
import com.pavco.org.service.mapper.BillMapper;
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
 * Service Implementation for managing {@link com.pavco.org.domain.Bill}.
 */
@Service
@Transactional
public class BillServiceImpl implements BillService {

    private final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    public BillServiceImpl(BillRepository billRepository, BillMapper billMapper) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
    }

    @Override
    public BillDTO save(BillDTO billDTO) {
        log.debug("Request to save Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    @Override
    public BillDTO update(BillDTO billDTO) {
        log.debug("Request to update Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    @Override
    public Optional<BillDTO> partialUpdate(BillDTO billDTO) {
        log.debug("Request to partially update Bill : {}", billDTO);

        return billRepository
            .findById(billDTO.getId())
            .map(existingBill -> {
                billMapper.partialUpdate(existingBill, billDTO);

                return existingBill;
            })
            .map(billRepository::save)
            .map(billMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillDTO> findAll() {
        log.debug("Request to get all Bills");
        return billRepository.findAll().stream().map(billMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<BillDTO> findAllWithEagerRelationships(Pageable pageable) {
        return billRepository.findAllWithEagerRelationships(pageable).map(billMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillDTO> findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        return billRepository.findOneWithEagerRelationships(id).map(billMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bill : {}", id);
        billRepository.deleteById(id);
    }
}
