package coco.project.demo.repository;

import coco.project.demo.models.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRespository extends JpaRepository<PostImage, Long> {
}
