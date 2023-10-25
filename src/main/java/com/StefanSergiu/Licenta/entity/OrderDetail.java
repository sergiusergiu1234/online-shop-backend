package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private OrderItem orderItem;

    private Long quantity;
    private Float price;
    private String size;
}
