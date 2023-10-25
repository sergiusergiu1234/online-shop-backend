package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.ProductRepository;
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
    private ProductRepository productRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public ShoppingCart createShoppingCart(Integer userId, Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found!"));
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User with id+ "+ userId + " doesn't exist!"));


        //generate composite ShoppingCart key
        ShoppingCartKey key = new ShoppingCartKey();
        key.setProductId(productId);
        key.setUserId(userId);

        //check if combination already exists
        if(shoppingCartRepository.findById(key).isPresent()){
            ShoppingCart shoppingCart = shoppingCartRepository.findById(key).orElseThrow();
            Long quantity = shoppingCartRepository.findById(key).get().getQuantity();
            shoppingCartRepository.findById(key).get().setQuantity(quantity+1);
            shoppingCart.setPrice((quantity+1)*product.getPrice());
            return shoppingCartRepository.save(shoppingCart);
        }
        else{
            //create new ShopppingCart
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(key);
            shoppingCart.setUser(user);
            shoppingCart.setProduct(product);
            shoppingCart.setQuantity(1L);
            shoppingCart.setPrice(shoppingCart.getProduct().getPrice()* shoppingCart.getQuantity());
            return shoppingCartRepository.save(shoppingCart);
        }
    }

    @Transactional
    public ShoppingCart deleteShoppingCart(ShoppingCartKey id){
        Product product = productRepository.findById(id.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id.getProductId() + " not found!"));
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("favorite with id" + id + " not found"));
        if(shoppingCart.getQuantity()==1){
            shoppingCartRepository.delete(shoppingCart);
            return shoppingCart;
        }else{
            Long quantity = shoppingCart.getQuantity();
            shoppingCartRepository.findById(id).get().setQuantity(quantity-1);
            shoppingCart.setPrice((quantity-1)*product.getPrice());
            System.out.println(shoppingCart.getPrice());
            return shoppingCartRepository.save(shoppingCart);
        }

    }

    public List<ShoppingCart> getCartsByUser(Integer userId){
        return StreamSupport.stream(shoppingCartRepository.findByUserId(userId).spliterator(),false)
                .collect(Collectors.toList());
    }
}
