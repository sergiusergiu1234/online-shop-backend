package com.StefanSergiu.Licenta.repository;

//import com.StefanSergiu.Licenta.entity.ProductAttributeKey;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import com.StefanSergiu.Licenta.entity.ProductAttributeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long>, JpaSpecificationExecutor<ProductAttribute> {
    Optional<ProductAttribute> findById(ProductAttributeKey key);
}
