package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.ShoppingCart;
import com.StefanSergiu.Licenta.entity.ShoppingCartKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartKey> {

    Optional<ShoppingCart> findById (ShoppingCartKey key);

    Iterable<ShoppingCart> findByUserId(Integer id);
}
