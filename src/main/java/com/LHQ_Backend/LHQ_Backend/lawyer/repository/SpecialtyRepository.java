package com.LHQ_Backend.LHQ_Backend.lawyer.repository;

import com.LHQ_Backend.LHQ_Backend.lawyer.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, String> {

    Optional<Specialty> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    /**
     * Bulk fetch by IDs — used when creating/updating a LawyerProfile to resolve the Set<String>
     * specialtyIds from the request DTO.
     */
    @Query("SELECT s FROM Specialty s WHERE s.id IN :ids")
    Set<Specialty> findAllByIdIn(@Param("ids") Set<String> ids);

    /**
     * Returns all specialties that have at least one lawyer assigned. Useful for filtering UIs that
     * should not show empty categories.
     */
    @Query("""
            SELECT DISTINCT s FROM Specialty s
            JOIN s.lawyerProfiles lp
            """)
    List<Specialty> findAllWithAtLeastOneLawyer();
}
