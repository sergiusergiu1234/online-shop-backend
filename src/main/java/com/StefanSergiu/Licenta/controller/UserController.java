package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.config.AuthRequest;
import com.StefanSergiu.Licenta.dto.user.AuthResponse;
import com.StefanSergiu.Licenta.dto.user.EditDataDto;
import com.StefanSergiu.Licenta.dto.user.UserDto;
import com.StefanSergiu.Licenta.entity.UserInfo;
import com.StefanSergiu.Licenta.service.JwtService;
import com.StefanSergiu.Licenta.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:3000","https://slope-emporium.vercel.app"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public String addNewUser(@RequestBody UserInfo userInfo){
        return userService.addUser(userInfo);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/allUsers")
    public List<UserInfo> getAllUsers (){return userService.viewAllUsers();}


    @PostMapping("/signin")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtService.generateToken(authRequest.getUsername()));
            authResponse.setRole(authentication.getAuthorities().stream().findFirst().get().getAuthority());
            return authResponse;
        }else{
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();    //get users name
        UserInfo user = userService.getLoggedInUser(username); //get userInfo object based on logged-in user's username
        return (new ResponseEntity<>(UserDto.from(user), HttpStatus.OK));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = new Cookie("jwt",null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout succesfull");
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/me/edit/password")
    public String editPassword(@RequestBody EditDataDto text){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();    //get users name
        UserInfo user = userService.getLoggedInUser(username); //get userInfo object based on logged-in user's username
        String message = userService.editUserPassword(user, text.getUpdateText());
        return message;
    }


}

