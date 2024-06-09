package com.pavco.org.repository;

import com.pavco.org.domain.BillFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillFile entity.
 */
@Repository
public interface BillFileRepository extends JpaRepository<BillFile, Long> {
    default Optional<BillFile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BillFile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BillFile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select billFile from BillFile billFile left join fetch billFile.client",
        countQuery = "select count(billFile) from BillFile billFile"
    )
    Page<BillFile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select billFile from BillFile billFile left join fetch billFile.client")
    List<BillFile> findAllWithToOneRelationships();

    @Query("select billFile from BillFile billFile left join fetch billFile.client where billFile.id =:id")
    Optional<BillFile> findOneWithToOneRelationships(@Param("id") Long id);
}
