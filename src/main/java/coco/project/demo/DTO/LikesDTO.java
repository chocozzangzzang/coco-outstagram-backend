package coco.project.demo.DTO;

import coco.project.demo.models.Post;
import coco.project.demo.models.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikesDTO {
    public Long id;
    private Long postId;
    private Long userId;

}
