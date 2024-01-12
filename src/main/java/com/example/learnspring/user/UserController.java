package com.example.learnspring.user;


import com.example.learnspring.models.User;
import com.example.learnspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {

        System.out.print("asdasd");
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userRepository.findById(id).get();
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }

    @PostMapping("/upload/{id}")
    public User uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        User user = userRepository.findById(id).get();
        user.setImage(fileName);

        User savedUser = userRepository.save(user);

        String uploadDir = "user-photos/" + savedUser.getId();

        String path = StorageUtil.saveFile(uploadDir, fileName, multipartFile);

        return savedUser;
    }
}

