package coco.project.demo.DTO;

import coco.project.demo.models.Post;
import coco.project.demo.models.User;
import lombok.*;

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
    public String content;
}
