package com.StefanSergiu.Licenta.repository;

import com.StefanSergiu.Licenta.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Iterable<OrderDetail> findByOrderId(Long orderId);
}
