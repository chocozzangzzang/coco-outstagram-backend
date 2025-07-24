package coco.project.demo.service;

import coco.project.demo.DTO.PostDTO;
import coco.project.demo.DTO.PostImageDTO;
import coco.project.demo.models.Post;
import coco.project.demo.models.PostImage;
import coco.project.demo.repository.PostImageRespository;
import coco.project.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostImageRespository postImageRespository;

    @Override
    @Transactional
    public Post createFeed(PostDTO postDTO) {
        System.out.println(postDTO);
        Post post = Post.builder()
                .content(postDTO.getContent())
                .writer(postDTO.getWriter())
                .createdAt(postDTO.getCreatedAt())
                .build();

        postRepository.save(post);

        for(PostImageDTO postImageDTO : postDTO.getPostImages()) {
            PostImage postImage = PostImage.builder()
                    .imageIndex(postImageDTO.getImageIdx())
                    .imageUrl(postImageDTO.getImageUrl())
                    .fileName(postImageDTO.getFileName())
                    .build();

            post.addImage(postImage);
        }

        return post;
    }

    @Override
    @Transactional
    public PostImage imageUploader(PostImageDTO postImageDTO) {
        PostImage postImage = PostImage.builder()
                .imageIndex(postImageDTO.getImageIdx())
                .imageUrl(postImageDTO.getImageUrl())
                .fileName(postImageDTO.getFileName())
                .build();

        return postImageRespository.save(postImage);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllWithImages();
    }
}
