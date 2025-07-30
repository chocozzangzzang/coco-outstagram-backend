package coco.project.demo.repository;

import coco.project.demo.models.Follow;
import coco.project.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 팔로우 관계 확인
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 특정 사용자가 다른 사용자를 팔로우하는지?
    boolean existsByFollowerAndFollowing(User follower, User following);

}
