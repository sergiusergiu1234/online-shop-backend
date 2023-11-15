package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.FavoriteRepository;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.repository.ProductSizeRepository;
import com.StefanSergiu.Licenta.repository.UserInfoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public Favorite getFavorite(FavoriteKey id){
        return favoriteRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Favorite with id "+ id+ " not found!"));
    }

    @Transactional
    public Favorite createFavorite(Integer userId, Long productSizeId){
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(() -> new EntityNotFoundException("Product size not found"));
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User with id+ "+ userId + " doesn't exist!"));

        //create new Favorite
        Favorite favorite = new Favorite();
        FavoriteKey key = new FavoriteKey();
        key.setUserId(userId);
        key.setProductSizeId(productSizeId);

        favorite.setId(key);
        favorite.setUser(user);
        favorite.setProductSize(productSize);

        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavorites() {
        return StreamSupport.stream(favoriteRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }


    public List<Favorite> getFavoriteByUser(Integer userId){
        return StreamSupport.stream(favoriteRepository.findByUserId(userId).spliterator(),false)
                .collect(Collectors.toList());
    }

    @Transactional
    public Favorite deleteFavorite(FavoriteKey id) {
        Favorite favorite= favoriteRepository.findById(id).orElseThrow(()->new EntityNotFoundException("favorite with id "+ id + " not found"));
        favoriteRepository.delete(favorite);
        return favorite;
    }
}
