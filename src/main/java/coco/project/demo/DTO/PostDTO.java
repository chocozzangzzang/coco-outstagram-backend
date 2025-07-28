package coco.project.demo.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    public Long id;
    public String content;
    public String writer;
    public List<PostImageDTO> postImages;
    public List<LikesDTO> likes;
    public List<CommentDTO> comments;
    public LocalDateTime createdAt;
}
