package com.pavco.org.repository;

import com.pavco.org.domain.BillDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillDetail entity.
 */
@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    default Optional<BillDetail> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BillDetail> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BillDetail> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select billDetail from BillDetail billDetail left join fetch billDetail.bill",
        countQuery = "select count(billDetail) from BillDetail billDetail"
    )
    Page<BillDetail> findAllWithToOneRelationships(Pageable pageable);

    @Query("select billDetail from BillDetail billDetail left join fetch billDetail.bill")
    List<BillDetail> findAllWithToOneRelationships();

    @Query("select billDetail from BillDetail billDetail left join fetch billDetail.bill where billDetail.id =:id")
    Optional<BillDetail> findOneWithToOneRelationships(@Param("id") Long id);
}
