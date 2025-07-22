package coco.project.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "postImages")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long postId;

    @Column(nullable = false)
    public Long imageIndex;

    @Column(nullable = false)
    public String imageUrl;

    @Column(nullable = false)
    public String fileName;

}
