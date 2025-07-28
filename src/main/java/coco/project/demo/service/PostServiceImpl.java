package coco.project.demo.service;

import coco.project.demo.DTO.CommentDTO;
import coco.project.demo.DTO.LikesDTO;
import coco.project.demo.DTO.PostDTO;
import coco.project.demo.DTO.PostImageDTO;
import coco.project.demo.models.Comment;
import coco.project.demo.models.Likes;
import coco.project.demo.models.Post;
import coco.project.demo.models.PostImage;
import coco.project.demo.repository.PostImageRespository;
import coco.project.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllWithImages();
        List<PostDTO> postDTOs = new ArrayList<>();
        for(Post post : posts) {
            PostDTO tempPostDTO = new PostDTO();
            tempPostDTO.setId(post.getId());
            tempPostDTO.setContent(post.getContent());
            tempPostDTO.setWriter(post.getWriter());
            List<PostImageDTO> postImageDTOs = post.getPostImages().stream()
                    .map(postImage -> {
                        return new PostImageDTO(postImage.getId(), postImage.getImageIndex(), postImage.getImageUrl(), postImage.getFileName());
                    })
                    .collect(Collectors.toList());
            List<LikesDTO> likesDTOs = post.getLikes().stream()
                    .map(like -> {
                        Long userId = like.getUser().getId();
                        Long postId = like.getPost().getId();
                       return new LikesDTO(like.getId(), postId, userId);
                    })
                    .collect(Collectors.toList());
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> {
                        Long userId = comment.getUser().getId();
                        return new CommentDTO(comment.getId(), post.getId(), userId, comment.getContent());
                    })
                    .collect(Collectors.toList());
            postDTOs.add(new PostDTO(
                    post.getId(),
                    post.getContent(),
                    post.getWriter(),
                    postImageDTOs,
                    likesDTOs,
                    commentDTOs,
                    post.getCreatedAt()
            ));
        }
        return postDTOs;
    }
}
