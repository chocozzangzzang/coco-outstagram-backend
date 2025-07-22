package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PostImageDTO {
    public Long postId;
    public Long imageIdx;
    public String imageUrl;
    public String fileName;
}
