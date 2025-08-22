package coco.project.demo.service;

import coco.project.demo.DTO.*;
import coco.project.demo.models.User;
import coco.project.demo.models.UserRole;
import coco.project.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User registerUser(RegisterDTO registerDTO) {

        if(userRepository.existsByUsername(registerDTO.username)) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        if(userRepository.existsByEmail(registerDTO.email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .firebaseUid(registerDTO.getFirebaseUid())
                .role(UserRole.USER)
                .build();
        System.out.println(user);


        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User loginUser(LoginDTO loginDTO) {

        if(!userRepository.existsByEmail(loginDTO.getEmail())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        User findUser = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return findUser;
    }

    @Override
    public UserDTO getProfileWithUsername(String username) {
        if(!userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        Optional<User> findUser = userRepository.findByUsername(username);

        if(findUser.isEmpty()) throw new IllegalArgumentException("사용자가 존재하지 않습니다.");

        return User.userEntityToDTO(findUser.get());
    }

    @Override
    public User profileImageUpdate(ProfileImageDTO profileImageDTO) {
        if(!userRepository.existsByUsername(profileImageDTO.getUsername())) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        User findUser = userRepository.findByUsername(profileImageDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        User updatedUser = User.builder()
                .id(findUser.getId())
                .username(profileImageDTO.getUsername())
                .email(findUser.getEmail())
                .profilePictureName(profileImageDTO.getFilename())
                .profilePictureUrl(profileImageDTO.getFileurl())
                .role(findUser.getRole())
                .createdAt(findUser.getCreatedAt())
                .firebaseUid(findUser.getFirebaseUid())
                .build();

        return userRepository.save(updatedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

        List<UserDTO> userDTOs = userList.stream().map(
                user -> {
                    List<LikesDTO> likesDTOs = user.getLikes().stream()
                            .map(likes -> {
                                return new LikesDTO(likes.getId(), likes.getPost().getId(), likes.getUser().getId());
                            }).collect(Collectors.toList());

                    List<CommentDTO> commentDTOs = user.getComments().stream()
                            .map(comment -> {
                                return new CommentDTO(comment.getId(), comment.getPost().getId(), comment.getUser().getId(), comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt());
                            }).collect(Collectors.toList());

                    return new UserDTO(
                            user.getId(), user.getUsername(),
                            user.getEmail(), user.getRole(),
                            user.getProfilePictureName(), user.getProfilePictureUrl(),
                            likesDTOs, commentDTOs,
                            user.getCreatedAt(),
                            user.getFirebaseUid()
                            );
                }
        ).collect(Collectors.toList());

        return userDTOs;
    }
}
