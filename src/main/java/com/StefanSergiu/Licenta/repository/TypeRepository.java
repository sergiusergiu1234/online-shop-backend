package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type,Long> {
    Type findByName(String typeName);

    Type findByCategories_Name(String categoryName);

}
