package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDTO {

    public String email;
    public String firebaseUid;

}
