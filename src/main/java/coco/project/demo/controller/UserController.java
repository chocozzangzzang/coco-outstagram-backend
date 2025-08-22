package coco.project.demo.controller;

import coco.project.demo.DTO.*;
import coco.project.demo.JWT.JwtResponse;
import coco.project.demo.JWT.TokenInfo;
import coco.project.demo.models.Follow;
import coco.project.demo.models.User;
import coco.project.demo.service.FollowService;
import coco.project.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @PostMapping("/following")
    public ResponseEntity<?> follow(@Valid @RequestBody FollowDTO followDTO) {
        try {
            Follow follow = followService.followUser(followDTO);
            FollowDTO follow_dto = FollowDTO.builder()
                    .id(follow.getId())
                    .followingId(follow.getFollowing().getId())
                    .followerId(follow.getFollower().getId())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(follow_dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("팔로우를 실패했습니다. " + e.getMessage());
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollow(@Valid @RequestBody FollowDTO followDTO) {
        try {
            followService.unFollowUser(followDTO);
            return ResponseEntity.status(HttpStatus.OK).body("팔로우를 해제하였습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("팔로우를 해제하지 못했습니다. " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원이 없습니다. " + e.getMessage());
        }
    }

    @GetMapping("/follows")
    public ResponseEntity<?> getFollowList() {
        try {
            List<FollowDTO> follows = followService.getAllFollows();
            return ResponseEntity.ok(follows);
        }   catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("목록을 받을 수 없음! " + e.getMessage());
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<?> getProfile(@Valid @RequestBody ProfileDTO profileDTO) {
        try {
            UserDTO userDTO = userService.getProfileWithUsername(profileDTO.getUsername());
            return ResponseEntity.ok(userDTO);
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 오류 " + e.getMessage());
        }
    }

    @PutMapping("/profileImage")
    public ResponseEntity<?> profileImageUpdater(@Valid @RequestBody ProfileImageDTO profileImageDTO) {
        try {
            userService.profileImageUpdate(profileImageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("프로필이미지를 업데이트했습니다. " + profileImageDTO.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 오류 " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO,
                                        @RequestHeader("Authorization") String idToken) {
        try {
            // Bearer 제거
            String token = idToken.substring(7);

            // idToken 검증
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            // uid 추출
            String firebaseUid = decodedToken.getUid();

            // registerDTO에 저장
            registerDTO.setFirebaseUid(firebaseUid);

            userService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다.");
        } catch (FirebaseAuthException e) {
            System.err.println("firebase "  + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 오류 " + e.getMessage());
        }
    }

    private final AuthenticationManager authenticationManager;
    private final TokenInfo tokenInfo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO,
                                   @RequestHeader("Authorization") String idToken) {
        try {
            String token = idToken.substring(7);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();
            loginDTO.setFirebaseUid(firebaseUid);

            User login = userService.loginUser(loginDTO);

            String jwt = tokenInfo.generateJwtToken(login.getUsername());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("email", login.getEmail());
            responseBody.put("jwt", new JwtResponse(jwt, login.getUsername()));
            responseBody.put("id", login.getId());
            return ResponseEntity.ok(responseBody);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 오류 " + e.getMessage());
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

