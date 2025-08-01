package coco.project.demo.service;

import coco.project.demo.DTO.FollowDTO;
import coco.project.demo.models.Follow;
import coco.project.demo.models.User;
import coco.project.demo.repository.FollowRepository;
import coco.project.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public List<FollowDTO> getAllFollows() {
        List<Follow> follows = followRepository.findAll();

        List<FollowDTO> followDTOs = follows.stream().map(
                follow -> {
                    return new FollowDTO(follow.getId(), follow.getFollower().getId(), follow.getFollowing().getId());
                }
        ).collect(Collectors.toList());

        return followDTOs;
    }

    @Override
    public Follow followUser(FollowDTO followDTO) {
        User following = userRepository
                .findById(followDTO.getFollowingId())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));
        User follower = userRepository
                .findById(followDTO.getFollowerId())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        Follow follow = Follow.builder()
                .following(following)
                .follower(follower)
                .build();

        return followRepository.save(follow);
    }

    @Override
    public void unFollowUser(FollowDTO followDTO) {
        User following = userRepository.findById(followDTO.getFollowingId())
                        .orElseThrow(() -> new IllegalArgumentException("없는 아이디입니다."));
        User follower = userRepository.findById(followDTO.getFollowerId())
                        .orElseThrow(() -> new IllegalArgumentException("없는 아이디입니다."));

        followRepository.deleteById(followDTO.getId());
    }
}
