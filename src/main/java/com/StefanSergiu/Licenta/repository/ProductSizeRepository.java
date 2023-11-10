package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.ProductSize;
import com.StefanSergiu.Licenta.entity.ProductSizeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
   Optional<ProductSize> findById(ProductSizeKey id);



   List<ProductSize> findAllByProductId(Long productId);
}
