package coco.project.demo.service;

import coco.project.demo.DTO.FollowDTO;
import coco.project.demo.models.Follow;
import coco.project.demo.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
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
}
