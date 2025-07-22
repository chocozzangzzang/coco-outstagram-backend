package coco.project.demo.service;

import coco.project.demo.DTO.PostDTO;
import coco.project.demo.DTO.PostImageDTO;
import coco.project.demo.models.Post;
import coco.project.demo.models.PostImage;

public interface PostService {
    public Post createFeed(PostDTO postDTO);

    public PostImage imageUploader(PostImageDTO postImageDTO);
}
