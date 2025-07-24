package coco.project.demo.models;

import coco.project.demo.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, unique = true, length = 50)
    public String username;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false, unique = true, length = 100)
    public String email;
    @Column(length = 255)
    public String profilePictureName;
    @Column(length = 255)
    public String profilePictureUrl;
    @Column(nullable = false)
    public UserRole role;
    private LocalDateTime createdAt;

    public static UserDTO userEntityToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureName(user.getProfilePictureName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
