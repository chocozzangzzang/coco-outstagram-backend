package coco.project.demo.controller;

import coco.project.demo.DTO.*;
import coco.project.demo.JWT.JwtResponse;
import coco.project.demo.JWT.TokenInfo;
import coco.project.demo.service.FollowService;
import coco.project.demo.service.UserService;
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
        System.out.println(profileImageDTO.getUsername() + "---" + profileImageDTO.getFilename() + "--" + profileImageDTO.getFileurl());
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            userService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다. " + registerDTO.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 오류 " + e.getMessage());
        }
    }

    private final AuthenticationManager authenticationManager;
    private final TokenInfo tokenInfo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
             UserDTO userDTO = userService.loginUser(loginDTO);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), loginDTO.getPassword())
            );
            // 인증 성공 시 SecurityContext에 Authentication 객체를 저장함 //
            SecurityContextHolder.getContext().setAuthentication(auth);
            // jwt 토큰
            String jwt = tokenInfo.generateJwtToken(userDTO.getUsername());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("jwt", new JwtResponse(jwt, auth.getName()));
            responseBody.put("id", userDTO.getId());
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

