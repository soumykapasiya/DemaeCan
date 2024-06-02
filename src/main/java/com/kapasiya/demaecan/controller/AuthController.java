package com.kapasiya.demaecan.controller;

import com.kapasiya.demaecan.config.JwtProvider;
import com.kapasiya.demaecan.model.Cart;
import com.kapasiya.demaecan.model.USER_ROLE;
import com.kapasiya.demaecan.model.User;
import com.kapasiya.demaecan.repositories.CartReository;
import com.kapasiya.demaecan.repositories.UserRepository;
import com.kapasiya.demaecan.request.LoginRequest;
import com.kapasiya.demaecan.response.AuthReponse;
import com.kapasiya.demaecan.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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


    @PostMapping("/signup")
    public ResponseEntity<AuthReponse> createUserHandler(@RequestBody User user) throws Exception
    {
        User isEmailExists = userRepository.findByEmail(user.getEmail());
        if (isEmailExists != null)
        {
            throw  new Exception("Email is already exist with another account.....");
        }

        User createdUser = new User();

        createdUser.setEmail(user.getEmail());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());

        User savedUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartReository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthReponse authReponse = new AuthReponse();

        authReponse.setJwt(jwt);
        authReponse.setMessage("Registered Successfully");
        authReponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authReponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthReponse> signIn(@RequestBody LoginRequest request) throws Exception
    {
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(username,password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();


        String jwt = jwtProvider.generateToken(authentication);

        AuthReponse authResponse = new AuthReponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("Registered Successfully............");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

    public Authentication authenticate(String username, String password)
    {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (userDetails==null)
        {
           throw new BadCredentialsException("Invalid Username.........");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword()))
        {
            throw new BadCredentialsException("Invalid Password.........");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
