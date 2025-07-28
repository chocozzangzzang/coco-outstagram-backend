package coco.project.demo.DTO;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostImageDTO {
    public Long id;
    public Long imageIdx;
    public String imageUrl;
    public String fileName;
}
