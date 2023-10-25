package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
