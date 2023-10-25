package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Orders{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    private Float total;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generation_date_time")
    private LocalDateTime generationDateTime;

    private String status;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();


    private String deliveryAddress;
    private String paymentMethod;
    private String billingName;
    private String contactPhone;
}
