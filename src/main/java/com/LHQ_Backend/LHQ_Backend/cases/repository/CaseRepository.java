package com.LHQ_Backend.LHQ_Backend.cases.repository;

import com.LHQ_Backend.LHQ_Backend.cases.entity.Case;
import com.LHQ_Backend.LHQ_Backend.cases.enums.CaseStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {

        Page<Case> findAllByClientLawyerId(String clientLawyerId, Pageable pageable);

        Page<Case> findAllByClientLawyerIdAndStatus(String clientLawyerId, CaseStatus status,
                        Pageable pageable);

        /**
         * All cases belonging to a user (via the ClientLawyer join). Used on the client-facing
         * dashboard.
         */
        @Query("""
                        SELECT c FROM Case c
                        WHERE c.clientLawyer.user.id = :userId
                        """)
        Page<Case> findAllByUserId(@Param("userId") String userId, Pageable pageable);

        /**
         * All cases assigned to a lawyer (via the ClientLawyer join). Used on the lawyer-facing
         * dashboard.
         */
        @Query("""
                        SELECT c FROM Case c
                        WHERE c.clientLawyer.lawyer.id = :lawyerId
                        """)
        Page<Case> findAllByLawyerId(@Param("lawyerId") String lawyerId, Pageable pageable);

        @Query("""
                        SELECT c FROM Case c
                        WHERE c.clientLawyer.user.id = :userId
                          AND c.status = :status
                        """)
        Page<Case> findAllByUserIdAndStatus(@Param("userId") String userId,
                        @Param("status") CaseStatus status, Pageable pageable);

        @Query("""
                        SELECT c FROM Case c
                        WHERE c.clientLawyer.lawyer.id = :lawyerId
                          AND c.status = :status
                        """)
        Page<Case> findAllByLawyerIdAndStatus(@Param("lawyerId") String lawyerId,
                        @Param("status") CaseStatus status, Pageable pageable);

        /**
         * Ownership check. A case is accessible if the requesting principal is either the client or
         * the assigned lawyer on the linked ClientLawyer record.
         */
        @Query("""
                        SELECT c FROM Case c
                        WHERE c.id = :caseId
                          AND (c.clientLawyer.user.id = :principalId
                               OR c.clientLawyer.lawyer.id = :principalId)
                        """)
        java.util.Optional<Case> findByIdAndPrincipal(@Param("caseId") String caseId,
                        @Param("principalId") String principalId);

        /**
         * Full graph fetch for mapping to CaseResponse. Initializes: case → clientLawyer → user,
         * lawyer, lawyer.user Used by: any service method that returns CaseResponse
         */
        @Query("""
                        SELECT c FROM Case c
                        JOIN FETCH c.clientLawyer cl
                        JOIN FETCH cl.user
                        JOIN FETCH cl.lawyer l
                        JOIN FETCH l.user
                        WHERE c.id = :id
                        """)
        Optional<Case> findByIdWithFullGraph(@Param("id") String id);

        /**
         * Paginated list by clientLawyerId with full graph.
         */
        @Query(value = """
                        SELECT c FROM Case c
                        JOIN FETCH c.clientLawyer cl
                        JOIN FETCH cl.user
                        JOIN FETCH cl.lawyer l
                        JOIN FETCH l.user
                        WHERE cl.id = :clientLawyerId
                        """, countQuery = """
                        SELECT COUNT(c) FROM Case c
                        WHERE c.clientLawyer.id = :clientLawyerId
                        """)
        Page<Case> findAllByClientLawyerIdWithFullGraph(
                        @Param("clientLawyerId") String clientLawyerId, Pageable pageable);

        /**
         * Paginated list for a user with full graph.
         */
        @Query(value = """
                        SELECT c FROM Case c
                        JOIN FETCH c.clientLawyer cl
                        JOIN FETCH cl.user
                        JOIN FETCH cl.lawyer l
                        JOIN FETCH l.user
                        WHERE cl.user.id = :userId
                        """, countQuery = """
                        SELECT COUNT(c) FROM Case c
                        WHERE c.clientLawyer.user.id = :userId
                        """)
        Page<Case> findAllByUserIdWithFullGraph(@Param("userId") String userId, Pageable pageable);

        /**
         * Paginated list for a lawyer with full graph.
         */
        @Query(value = """
                        SELECT c FROM Case c
                        JOIN FETCH c.clientLawyer cl
                        JOIN FETCH cl.user
                        JOIN FETCH cl.lawyer l
                        JOIN FETCH l.user
                        WHERE cl.lawyer.id = :lawyerId
                        """, countQuery = """
                        SELECT COUNT(c) FROM Case c
                        WHERE c.clientLawyer.lawyer.id = :lawyerId
                        """)
        Page<Case> findAllByLawyerIdWithFullGraph(@Param("lawyerId") String lawyerId,
                        Pageable pageable);
}
