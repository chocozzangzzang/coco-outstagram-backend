package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class LikeBodyDTO {
    public Long id;
    public Long postId;
    public Long userId;
}
