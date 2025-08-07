package coco.project.demo.DTO;

import coco.project.demo.models.Post;
import coco.project.demo.models.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    public Long id;
    public Long postId;
    public Long userId;
    public String username;
    public String content;
    public LocalDateTime createdAt;
}
