package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class PostImageDTO {
    public Long imageIdx;
    public String imageUrl;
    public String fileName;
}
