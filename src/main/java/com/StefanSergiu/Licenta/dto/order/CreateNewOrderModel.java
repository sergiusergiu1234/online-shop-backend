package com.StefanSergiu.Licenta.dto.order;

import lombok.Data;

@Data
public class CreateNewOrderModel {
    private Float total;
    private String deliveryAddress;
    private String paymentMethod;
    private String billingName;
    private String contactPhone;

    //TODO: payment info + shipping address entities
}
