package coco.project.demo.service;

import coco.project.demo.DTO.LoginDTO;
import coco.project.demo.DTO.RegisterDTO;
import coco.project.demo.DTO.UserDTO;
import coco.project.demo.models.User;
import coco.project.demo.models.UserRole;
import coco.project.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        String encodePassword = passwordEncoder.encode(registerDTO.getPassword());

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(encodePassword)
                .email(registerDTO.getEmail())
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO loginUser(LoginDTO loginDTO) {

        if(!userRepository.existsByEmail(loginDTO.getEmail())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        Optional<User> findUser = userRepository.findByEmail(loginDTO.getEmail());

        if(!passwordEncoder.matches(loginDTO.getPassword(), findUser.get().getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        UserDTO userDTO = User.userEntityToDTO(findUser.get());

        return userDTO;
    }
}
