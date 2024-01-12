package com.example.learnspring.user;


import com.example.learnspring.dto.response.ErrorResponse;
import com.example.learnspring.models.User;
import com.example.learnspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity getAllUsers() {
         try {
             List<User> res = userRepository.findAll();
             return ResponseEntity.ok().body(res);
         }catch (Exception e){
             ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
         }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {

        try {
            User res = userRepository.findById(id).get();
            return ResponseEntity.ok().body(res);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User res = userRepository.findById(id).get();

            return ResponseEntity.ok().body(res);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

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

