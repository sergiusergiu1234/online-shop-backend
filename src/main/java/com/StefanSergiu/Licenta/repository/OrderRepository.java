package com.StefanSergiu.Licenta.repository;


import com.StefanSergiu.Licenta.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    Iterable<Orders> findByUserId(Integer userId);

    @Override
    List<Orders> findAll();
}
