package coco.project.demo.service;

import coco.project.demo.DTO.LoginDTO;
import coco.project.demo.DTO.ProfileImageDTO;
import coco.project.demo.DTO.RegisterDTO;
import coco.project.demo.DTO.UserDTO;
import coco.project.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User registerUser(RegisterDTO registerDTO);
    public User loginUser(LoginDTO loginDTO);
    public UserDTO getProfileWithUsername(String username);
    public User profileImageUpdate(ProfileImageDTO profileImageDTO);
    public List<UserDTO> getAllUsers();
}
