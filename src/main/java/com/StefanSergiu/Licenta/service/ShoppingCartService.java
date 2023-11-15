package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
import com.StefanSergiu.Licenta.repository.ProductSizeRepository;
import com.StefanSergiu.Licenta.repository.ShoppingCartRepository;
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
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public ShoppingCart createShoppingCart(Integer userId, Long productSizeId){
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productSizeId + " not found!"));
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User with id+ "+ userId + " doesn't exist!"));


        //generate composite ShoppingCart key
        ShoppingCartKey key = new ShoppingCartKey();
        key.setProductSizeId(productSizeId);
        key.setUserId(userId);

        //check if combination already exists
        if(shoppingCartRepository.findById(key).isPresent()){
            ShoppingCart shoppingCart = shoppingCartRepository.findById(key).orElseThrow();
            Long quantity = shoppingCartRepository.findById(key).get().getQuantity();
            shoppingCartRepository.findById(key).get().setQuantity(quantity+1);
            shoppingCart.setPrice((quantity+1)*productSize.getProduct().getPrice());
            return shoppingCartRepository.save(shoppingCart);
        }
        else{
            //create new ShopppingCart
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(key);
            shoppingCart.setUser(user);
            shoppingCart.setProductSize(productSize);
            shoppingCart.setQuantity(1L);
            shoppingCart.setPrice(shoppingCart.getProductSize().getProduct().getPrice()* shoppingCart.getQuantity());
            return shoppingCartRepository.save(shoppingCart);
        }
    }

    @Transactional
    public ShoppingCart deleteShoppingCart(ShoppingCartKey id){
        ProductSize productSize = productSizeRepository.findById(id.getProductSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id.getProductSizeId() + " not found!"));
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("favorite with id" + id + " not found"));
        if(shoppingCart.getQuantity()==1){
            shoppingCartRepository.delete(shoppingCart);
            return shoppingCart;
        }else{
            Long quantity = shoppingCart.getQuantity();
            shoppingCartRepository.findById(id).get().setQuantity(quantity-1);
            shoppingCart.setPrice((quantity-1)*productSize.getProduct().getPrice());
            System.out.println(shoppingCart.getPrice());
            return shoppingCartRepository.save(shoppingCart);
        }

    }

    public List<ShoppingCart> getCartsByUser(Integer userId){
        return StreamSupport.stream(shoppingCartRepository.findByUserId(userId).spliterator(),false)
                .collect(Collectors.toList());
    }
}
