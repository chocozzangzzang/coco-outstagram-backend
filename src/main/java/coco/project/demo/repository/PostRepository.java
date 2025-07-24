package coco.project.demo.repository;

import coco.project.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.postImages")
    List<Post> findAllWithImages();
}
