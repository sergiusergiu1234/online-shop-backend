package com.StefanSergiu.Licenta.dto.orderDetail;

import com.StefanSergiu.Licenta.entity.OrderDetail;
import lombok.Data;

@Data
public class OrderDetailDto {
    private String productName;
    private Long quantity;
    private Float price;
    private String size;
    public static OrderDetailDto from(OrderDetail orderDetail){
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductName(orderDetail.getOrderItem().getProductName());
        orderDetailDto.setPrice(orderDetail.getPrice());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        orderDetailDto.setSize(orderDetail.getSize());
        return orderDetailDto;
    }
}
