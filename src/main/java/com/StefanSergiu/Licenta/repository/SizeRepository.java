package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size,Long> {

}
