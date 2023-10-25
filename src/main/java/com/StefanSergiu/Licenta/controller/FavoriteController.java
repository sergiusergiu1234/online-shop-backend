package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.favorite.FavoriteDto;
import com.StefanSergiu.Licenta.entity.Favorite;
import com.StefanSergiu.Licenta.entity.FavoriteKey;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.UserInfo;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.service.FavoriteService;
import com.StefanSergiu.Licenta.service.FileStore;
import com.StefanSergiu.Licenta.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    UserService userService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    FileStore fileStore;
    @GetMapping("/all")
    public ResponseEntity<List<FavoriteDto>> getFavorites(){
        List<Favorite> favorites = favoriteService.getFavorites();

        List<FavoriteDto> favoriteDtoList = favorites.stream().map(FavoriteDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(favoriteDtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/add/{productId}")
    public ResponseEntity<FavoriteDto> newFavorite(@PathVariable final Long productId){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();
        ////
        Favorite favorite = favoriteService.createFavorite(userId, productId);
        return new ResponseEntity<>(FavoriteDto.from(favorite),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping()
    public ResponseEntity<List<FavoriteDto>> getUserFavorites(){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();
        System.out.println(authentication);
        ////
        List<Favorite> favorites = favoriteService.getFavoriteByUser(userId);
        List<FavoriteDto> favoriteDtoList = new ArrayList<>();
        for (Favorite favorite: favorites){
            byte[] imageData = fileStore.download(favorite.getProduct().getImagePath(),favorite.getProduct().getImageFileName());
            FavoriteDto favoriteDto = FavoriteDto.from(favorite,imageData);
            favoriteDtoList.add(favoriteDto);
        }
        return new ResponseEntity<>(favoriteDtoList,HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FavoriteDto> deleteFavorite(@PathVariable final Long id){

        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();

        ////
        //verify if product exists by id
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with id " + id + " not found"));

        //create key
        FavoriteKey key = new FavoriteKey();
        key.setProductId(id);
        key.setUserId(userId);

        //delete by key
        Favorite favorite = favoriteService.deleteFavorite(key);

    return  new ResponseEntity<>(FavoriteDto.from(favorite),HttpStatus.OK);
    }

}
