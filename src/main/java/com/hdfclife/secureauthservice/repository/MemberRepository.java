package com.hdfclife.secureauthservice.repository;

import com.hdfclife.secureauthservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    /**
     * Fetch all userIds that already exist in the database from the given list of candidates.
     * This allows checking both the requested userId and generated suggestions in a single query.
     *
     * @param userIds List of userIds to check
     * @return List of existing userIds in DB
     */
    @Query("SELECT u.userId FROM Member u WHERE u.userId IN :userIds")
    List<String> findAllExistingUserIds(@Param("userIds") List<String> userIds);

    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}
