package com.example.learnspring.authentication;

import com.example.learnspring.config.JwtUtil;
import com.example.learnspring.dto.request.AuthenticationRequest;
import com.example.learnspring.dto.response.AuthenticationResponse;
import com.example.learnspring.dto.response.ErrorResponse;
import com.example.learnspring.models.User;
import com.example.learnspring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest req)  {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            String email = authentication.getName();
            User user = new User();
            String token = jwtUtil.createToken(user);
            AuthenticationResponse res = new AuthenticationResponse(email,token);

            return ResponseEntity.ok(res);

        }catch (BadCredentialsException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            userRepository.save(user);

            String token = jwtUtil.createToken(user);
            AuthenticationResponse res = new AuthenticationResponse(user.getEmail(),token);

            return ResponseEntity.ok(res);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
