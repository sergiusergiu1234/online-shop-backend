package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender,Long> {
    Gender findByName(String genderName);
}
