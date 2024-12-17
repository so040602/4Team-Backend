package com.recipe.Repository;

import com.recipe.entity.Follow;
import com.recipe.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(Member follower, Member following);
    long countByFollower(Member follower);  // 팔로잉 수
    long countByFollowing(Member following);  // 팔로워 수
    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);
    List<Follow> findByFollowing(Member following);
    List<Follow> findByFollower(Member follower);
}
