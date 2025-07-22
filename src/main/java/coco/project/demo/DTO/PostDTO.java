package coco.project.demo.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class PostDTO {
    public String content;
    public String writer;
    public LocalDateTime createdAt;
}
