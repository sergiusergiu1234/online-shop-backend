package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.config.AuthRequest;
import com.StefanSergiu.Licenta.config.AuthRequestR;
import com.StefanSergiu.Licenta.config.UserInfoUserDetails;
import com.StefanSergiu.Licenta.dto.user.AuthResponse;
import com.StefanSergiu.Licenta.dto.user.EditDataDto;
import com.StefanSergiu.Licenta.dto.user.UserDto;
import com.StefanSergiu.Licenta.entity.RefreshToken;
import com.StefanSergiu.Licenta.entity.UserInfo;
import com.StefanSergiu.Licenta.service.JwtService;
import com.StefanSergiu.Licenta.service.RefreshTokenService;
import com.StefanSergiu.Licenta.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"https://slope-emporium.vercel.app","http://localhost:3000"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo){
        return new ResponseEntity<>(userService.addUser((userInfo)),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/allUsers")
    public List<UserInfo> getAllUsers (){return userService.viewAllUsers();}


    @PostMapping("/signin")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtService.generateToken(authRequest.getUsername()));
            authResponse.setRole(authentication.getAuthorities().stream().findFirst().get().getAuthority());
            return authResponse;
        }else{
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/signinr")
    public ResponseEntity<?> authenticateUser(@Valid AuthRequestR authRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtService.generateToken(authRequest.getUsername()));
            authResponse.setRole(authentication.getAuthorities().stream().findFirst().get().getAuthority());
            UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(((UserInfoUserDetails) authentication.getPrincipal()).getId()) ;
            ResponseCookie jwtRefreshCookie = jwtService.generateRefreshJwtCookie(refreshToken.getToken());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtRefreshCookie.toString())
                    .body(authResponse);
        }else{
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request){
//        String refreshToken = jwtService.getJwtRefreshFromCookies(request);
//
//        if((refreshToken != null ) && (refreshToken.length() > 0)){
//            return refreshTokenService.findByToken(refreshToken)
//                    map
//        }
//    }

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
        Object prinicpal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(prinicpal.toString() != "anonymousUser"){
            int userId = ((UserInfoUserDetails) prinicpal).getId();
            refreshTokenService.deleteByUserId(userId);
        }
        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,jwtRefreshCookie.toString())
                .body("Signout succesful");
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

