package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.ShoppingCartDto;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.service.FileStore;
import com.StefanSergiu.Licenta.service.ShoppingCartService;
import com.StefanSergiu.Licenta.service.UserService;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@CrossOrigin(origins = "http://localhost:3000")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @Autowired
    FileStore fileStore;

    @Autowired
    ProductRepository productRepository;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping()
    public ResponseEntity<List<ShoppingCartDto>> getUserShoppingCart(){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();
        ////
        List<ShoppingCart> shoppingCarts = shoppingCartService.getCartsByUser(userId);
        List<ShoppingCartDto> shoppingCartDtoList =  new ArrayList<>();

        for(ShoppingCart shoppingCart : shoppingCarts) {
        //    byte[] imageData = fileStore.download(shoppingCart.getProduct().getImagePath(), shoppingCart.getProduct().getImageFileName());
            byte[] imageData =new byte[16000];
            ShoppingCartDto shoppingCartDto = ShoppingCartDto.from(shoppingCart, imageData);
            shoppingCartDtoList.add(shoppingCartDto);
        }
        return new ResponseEntity<>(shoppingCartDtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/add/{productId}")
    public ResponseEntity<ShoppingCartDto> newShoppingCart(@PathVariable final Long productId){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();
        ////
        ShoppingCart shoppingCart = shoppingCartService.createShoppingCart(userId,productId);
        return new ResponseEntity<>(ShoppingCartDto.from(shoppingCart),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ShoppingCartDto> deleteShoppingCart(@PathVariable final Long productId){

        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();
        ////

        //verify if product exists by id
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product with id " + productId + " not found"));

        //create key
        ShoppingCartKey key = new ShoppingCartKey();
        key.setProductId(productId);
        key.setUserId(userId);

        //delete by key
        ShoppingCart shoppingCart = shoppingCartService.deleteShoppingCart(key);


        return new ResponseEntity<>(ShoppingCartDto.from(shoppingCart),HttpStatus.OK);

    }

}
