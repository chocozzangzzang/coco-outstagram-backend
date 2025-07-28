package coco.project.demo.models;

import coco.project.demo.DTO.CommentDTO;
import coco.project.demo.DTO.LikesDTO;
import coco.project.demo.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(length = 255)
    private String profilePictureName;
    @Column(length = 255)
    private String profilePictureUrl;
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    public static UserDTO userEntityToDTO(User user) {

        List<LikesDTO> likeList = new ArrayList<>();
        for(Likes like : user.getLikes()) {
            LikesDTO tmpLike = new LikesDTO();
            tmpLike.setUserId(like.getUser().getId());
            tmpLike.setId(like.getId());
            tmpLike.setPostId(like.getPost().getId());
            likeList.add(tmpLike);
        }

        List<CommentDTO> commentList = new ArrayList<>();
        for(Comment comment : user.getComments()) {
            CommentDTO tmpComment = new CommentDTO();
            tmpComment.setId(comment.getId());
            tmpComment.setUserId(comment.getUser().getId());
            tmpComment.setPostId(comment.getPost().getId());
            commentList.add(tmpComment);
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureName(user.getProfilePictureName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole())
                .likes(likeList)
                .comments(commentList)
                .createdAt(user.getCreatedAt())
                .build();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
