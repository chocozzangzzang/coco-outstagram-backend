package coco.project.demo.controller;

import coco.project.demo.DTO.PostDTO;
import coco.project.demo.DTO.PostImageDTO;
import coco.project.demo.DTO.ProfileImageDTO;
import coco.project.demo.models.Post;
import coco.project.demo.models.PostImage;
import coco.project.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> register(@Valid @RequestBody PostDTO postDTO) {
        try {
            Post p = postService.createFeed(postDTO);
            Map<String, Long> responseBody = new HashMap<>();
            responseBody.put("postId", p.getId()); // Key is "postId", value is your Long
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 오류 " + e.getMessage());
        }

    }

    @PostMapping("/image")
    public ResponseEntity<?> imageUploader(@Valid @RequestBody PostImageDTO postImageDTO) {
        try {
            postService.imageUploader(postImageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("이미지를 업로드했습니다. " + postImageDTO.getPostId() + " / " + postImageDTO.getImageIdx());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 오류 " + e.getMessage());
        }
    }

    // @Valid 검증 실패 시 예외 처리 핸들러 (선택 사항, 필요에 따라 글로벌 예외 핸들러로 분리 가능)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidErrorExceptions(MethodArgumentNotValidException ex){
        StringBuilder error = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String errorMessage = err.getDefaultMessage();
            error.append(errorMessage).append("\n");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toString());
    }

}

