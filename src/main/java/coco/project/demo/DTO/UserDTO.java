package coco.project.demo.DTO;

import coco.project.demo.models.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserDTO {

    public Long id;
    public String username;
    public String email;
    public UserRole role;
    public String profilePictureName;
    public String profilePictureUrl;
    public List<LikesDTO> likes;
    public List<CommentDTO> comments;
    public LocalDateTime createdAt;
    public String firebaseUid;

}
