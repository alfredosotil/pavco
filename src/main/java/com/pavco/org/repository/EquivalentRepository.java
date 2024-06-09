package com.pavco.org.repository;

import com.pavco.org.domain.Equivalent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Equivalent entity.
 */
@Repository
public interface EquivalentRepository extends JpaRepository<Equivalent, Long> {
    default Optional<Equivalent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Equivalent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Equivalent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select equivalent from Equivalent equivalent left join fetch equivalent.product left join fetch equivalent.client",
        countQuery = "select count(equivalent) from Equivalent equivalent"
    )
    Page<Equivalent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select equivalent from Equivalent equivalent left join fetch equivalent.product left join fetch equivalent.client")
    List<Equivalent> findAllWithToOneRelationships();

    @Query(
        "select equivalent from Equivalent equivalent left join fetch equivalent.product left join fetch equivalent.client where equivalent.id =:id"
    )
    Optional<Equivalent> findOneWithToOneRelationships(@Param("id") Long id);
}
