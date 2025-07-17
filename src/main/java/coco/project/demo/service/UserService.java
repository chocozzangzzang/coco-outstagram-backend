package coco.project.demo.service;

import coco.project.demo.DTO.LoginDTO;
import coco.project.demo.DTO.RegisterDTO;
import coco.project.demo.DTO.UserDTO;
import coco.project.demo.models.User;

import java.util.Optional;

public interface UserService {
    public User registerUser(RegisterDTO registerDTO);
    public UserDTO loginUser(LoginDTO loginDTO);

}
