package coco.project.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProfileImageDTO {
    public String username;
    public String filename;
    public String fileurl;
}
