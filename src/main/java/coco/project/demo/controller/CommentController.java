package coco.project.demo.controller;

import coco.project.demo.DTO.CommentDTO;
import coco.project.demo.DTO.PostIdDTO;
import coco.project.demo.models.Comment;
import coco.project.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body("댓글을 삭제했습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentDTO commentDTO) {
        try {
            Comment comment = commentService.addComment(commentDTO);
            CommentDTO returnCommentDTO = new CommentDTO(
                    comment.getId(), comment.getPost().getId(), comment.getUser().getId(),
                    comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(returnCommentDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllComments(@Valid @RequestBody PostIdDTO postIdDTO) {
        try {
            List<Comment> comments = commentService.getAllComments(postIdDTO.getPostId());
            List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
               return new CommentDTO(
                       comment.getId(), comment.getPost().getId(), comment.getUser().getId(),
                       comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt()
               );
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(commentDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 실패 : " + e.getMessage());
        }
    }

}
