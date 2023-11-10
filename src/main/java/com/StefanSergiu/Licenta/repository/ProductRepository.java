package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Brand;
import com.StefanSergiu.Licenta.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByNameIgnoreCase(String name);
    long countByName(String name);
    List<Product> findAll(Specification<Product> spec);
    Page<Product>findAll(Specification<Product>spec, Pageable pageable);
    List<Product> findAllByName(String name);
    Boolean  existsByName(String name);
    Optional<Product> findByBrand(Brand brand);
    List<Product> findByBrandName(String brandName);

    List<Product> findByName(String productName);

}
