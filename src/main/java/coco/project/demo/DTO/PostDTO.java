package coco.project.demo.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@ToString
public class PostDTO {
    public String content;
    public String writer;
    public List<PostImageDTO> postImages;
    public LocalDateTime createdAt;
}
