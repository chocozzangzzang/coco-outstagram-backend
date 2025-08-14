package coco.project.demo.service;

import coco.project.demo.DTO.CommentDTO;
import coco.project.demo.models.Comment;
import coco.project.demo.models.Post;
import coco.project.demo.models.User;
import coco.project.demo.repository.CommentRepository;
import coco.project.demo.repository.PostRepository;
import coco.project.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment addComment(CommentDTO commentDTO) {

        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 달 수 없는 게시글입니다"));
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 달 수 없는 유저입니다."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentDTO.getContent())
                .createdAt(commentDTO.getCreatedAt())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
