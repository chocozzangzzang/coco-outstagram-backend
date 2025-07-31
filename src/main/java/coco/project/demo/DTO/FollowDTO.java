package coco.project.demo.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@AllArgsConstructor
@Builder
@ToString
public class FollowDTO {
    public Long id;
    public Long followerId;
    public Long followingId;
}
