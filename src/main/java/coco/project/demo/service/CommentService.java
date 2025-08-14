package coco.project.demo.service;

import coco.project.demo.DTO.CommentDTO;
import coco.project.demo.models.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {

    public Comment addComment(CommentDTO commentDTO);
    public List<Comment> getAllComments(Long PostId);
    public void deleteComment(Long commentId);
}
