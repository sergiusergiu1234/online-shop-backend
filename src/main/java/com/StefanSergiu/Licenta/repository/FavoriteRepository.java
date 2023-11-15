package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Favorite;
import com.StefanSergiu.Licenta.entity.FavoriteKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
     Optional<Favorite> findById(FavoriteKey key); //posibil optional

     Iterable<Favorite> findByUserId(Integer id);
}
