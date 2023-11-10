package com.StefanSergiu.Licenta.repository;


import com.StefanSergiu.Licenta.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
     Optional<Attribute> findById(Long id);



}
