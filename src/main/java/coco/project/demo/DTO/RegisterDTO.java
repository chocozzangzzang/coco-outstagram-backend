package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterDTO {

    public String username;
    public String email;
    public String password;
    public String firebaseUid;
}
