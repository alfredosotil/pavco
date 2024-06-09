package com.pavco.org.repository;

import com.pavco.org.domain.Bill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bill entity.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    default Optional<Bill> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Bill> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Bill> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select bill from Bill bill left join fetch bill.client", countQuery = "select count(bill) from Bill bill")
    Page<Bill> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bill from Bill bill left join fetch bill.client")
    List<Bill> findAllWithToOneRelationships();

    @Query("select bill from Bill bill left join fetch bill.client where bill.id =:id")
    Optional<Bill> findOneWithToOneRelationships(@Param("id") Long id);
}
