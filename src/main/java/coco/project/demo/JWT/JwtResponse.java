package coco.project.demo.JWT;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;

    public JwtResponse(String token, String username) {
        this.username = username;
        this.token = token;
    }
}
