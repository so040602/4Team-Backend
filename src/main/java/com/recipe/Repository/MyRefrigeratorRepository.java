package com.recipe.Repository;

import com.recipe.entity.MyRefrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyRefrigeratorRepository extends JpaRepository<MyRefrigerator, Long> {
    boolean existsByMemberMemberId(Long memberId);

    @Query("SELECT m.refriId from MyRefrigerator m where m.member.memberId = :memberId")
    Long findByMember_MemberId(@Param("memberId") Long memberId);
}
