package com.kapasiya.demaecan.controller;

import com.kapasiya.demaecan.config.JwtProvider;
import com.kapasiya.demaecan.model.User;
import com.kapasiya.demaecan.repositories.CartReository;
import com.kapasiya.demaecan.repositories.UserRepository;
import com.kapasiya.demaecan.response.AuthReponse;
import com.kapasiya.demaecan.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CartReository cartReository;

    public ResponseEntity<AuthReponse>createUserHandler(@RequestBody User user)
    {
        User isEmailExists = userRepository.findByEmail(user.getEmail());
        if (isEmailExists != null)
        {
            throw  new Exception("Email is already exist with another account.....")
        }

        User createdUser = new User();

        createdUser.setEmail(user.getEmail());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());

        return null;
    }

}
