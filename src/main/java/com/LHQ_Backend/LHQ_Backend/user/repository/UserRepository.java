package com.LHQ_Backend.LHQ_Backend.user.repository;

import com.LHQ_Backend.LHQ_Backend.user.entity.User;
import com.LHQ_Backend.LHQ_Backend.user.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAllByRole(Role role, Pageable pageable);

    /**
     * Case-insensitive full-name search across first + last name. Used by admin search endpoints.
     */
    @Query("""
            SELECT u FROM User u
            WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<User> searchByFullName(@Param("name") String name, Pageable pageable);
}
