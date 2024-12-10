package com.recipe.Repository;

import com.recipe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPrimaryEmail(String primaryEmail);
    Optional<Member> findByDisplayName(String displayName);
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByDisplayName(String displayName);
}
