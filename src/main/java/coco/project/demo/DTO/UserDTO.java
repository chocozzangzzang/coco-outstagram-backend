package coco.project.demo.DTO;

import coco.project.demo.models.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class UserDTO {

    public Long id;
    public String username;
    public String email;
    public UserRole role;
    public String profilePictureUrl;
    public LocalDateTime createdAt;

}
