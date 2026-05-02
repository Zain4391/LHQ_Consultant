package com.LHQ_Backend.LHQ_Backend.cases.repository;

import com.LHQ_Backend.LHQ_Backend.cases.entity.ClientLawyer;
import com.LHQ_Backend.LHQ_Backend.cases.enums.ClientLawyerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientLawyerRepository extends JpaRepository<ClientLawyer, String> {

    Page<ClientLawyer> findAllByUserId(String userId, Pageable pageable);

    Page<ClientLawyer> findAllByLawyerId(String lawyerId, Pageable pageable);

    Page<ClientLawyer> findAllByUserIdAndStatus(String userId, ClientLawyerStatus status,
            Pageable pageable);

    Page<ClientLawyer> findAllByLawyerIdAndStatus(String lawyerId, ClientLawyerStatus status,
            Pageable pageable);

    /**
     * Used before auto-creating a ClientLawyer record after a booking is CONFIRMED. The unique
     * constraint on (user_id, lawyer_id) will also guard at DB level, but this check prevents a
     * misleading ConstraintViolationException.
     */
    Optional<ClientLawyer> findByUserIdAndLawyerId(String userId, String lawyerId);

    boolean existsByUserIdAndLawyerId(String userId, String lawyerId);

    /**
     * Ownership check — ensures the requesting user (or lawyer) actually owns this relationship
     * before allowing status updates or case operations.
     */
    @Query("""
            SELECT cl FROM ClientLawyer cl
            WHERE cl.id = :id
              AND (cl.user.id = :principalId OR cl.lawyer.id = :principalId)
            """)
    Optional<ClientLawyer> findByIdAndPrincipal(@Param("id") String id,
            @Param("principalId") String principalId);
}
