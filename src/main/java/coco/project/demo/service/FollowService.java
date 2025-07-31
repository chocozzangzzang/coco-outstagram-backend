package coco.project.demo.service;

import coco.project.demo.DTO.*;
import coco.project.demo.models.User;

import java.util.List;

public interface FollowService {
    public List<FollowDTO> getAllFollows();
}
