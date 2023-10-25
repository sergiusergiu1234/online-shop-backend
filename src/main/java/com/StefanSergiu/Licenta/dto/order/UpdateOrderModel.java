package com.StefanSergiu.Licenta.dto.order;

import lombok.Data;

@Data
public class UpdateOrderModel {
    private Long orderId;
    private String newStatus;
}
