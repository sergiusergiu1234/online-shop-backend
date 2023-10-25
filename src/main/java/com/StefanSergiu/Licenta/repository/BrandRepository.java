package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);
}
