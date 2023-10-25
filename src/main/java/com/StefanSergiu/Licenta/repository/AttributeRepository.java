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

          //get a table eg: attributeName | attributeValues
     //                       --------------------------------------
//                            Color       |    Red, Blue
//                            Size        |    Small, Medium
     @Query("SELECT a.name as attributeName, GROUP_CONCAT(DISTINCT pa.value) as attributeValues " +
             "FROM Attribute a " +
             "JOIN ProductAttribute pa ON a.id = pa.attribute.id " +
             "JOIN Type t ON a.type.id = t.id " +
             "WHERE t.name = :typeName " +
             "GROUP BY a.name")
     List<Map<String, Object>> findAttributeValuesByTypeName(@Param("typeName") String typeName);

}
