package com.LHQ_Backend.LHQ_Backend.lawyer.repository;

import com.LHQ_Backend.LHQ_Backend.lawyer.entity.LawyerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LawyerProfileRepository extends JpaRepository<LawyerProfile, String> {

        Optional<LawyerProfile> findByUserId(String userId);

        boolean existsByUserId(String userId);

        boolean existsByBarNumber(String barNumber);

        /**
         * Fetches lawyer profiles WITH their specialties in a single query, avoiding N+1 on the
         * ManyToMany join table.
         */
        @Query("""
                        SELECT DISTINCT lp FROM LawyerProfile lp
                        LEFT JOIN FETCH lp.specialties
                        WHERE lp.id = :id
                        """)
        Optional<LawyerProfile> findByIdWithSpecialties(@Param("id") String id);

        /**
         * Filter lawyers by specialty name (case-insensitive). countQuery is required by Spring
         * Data when FETCH JOIN + Pageable are combined.
         */
        @Query(value = """
                        SELECT DISTINCT lp FROM LawyerProfile lp
                        JOIN lp.specialties s
                        WHERE LOWER(s.name) = LOWER(:specialtyName)
                        """, countQuery = """
                        SELECT COUNT(DISTINCT lp) FROM LawyerProfile lp
                        JOIN lp.specialties s
                        WHERE LOWER(s.name) = LOWER(:specialtyName)
                        """)
        Page<LawyerProfile> findAllBySpecialtyName(@Param("specialtyName") String specialtyName,
                        Pageable pageable);

        /**
         * Full-name search across the joined User entity.
         */
        @Query(value = """
                        SELECT lp FROM LawyerProfile lp
                        JOIN lp.user u
                        WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
                        """,
                        countQuery = """
                                        SELECT COUNT(lp) FROM LawyerProfile lp
                                        JOIN lp.user u
                                        WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
                                        """)
        Page<LawyerProfile> searchByLawyerFullName(@Param("name") String name, Pageable pageable);
}
