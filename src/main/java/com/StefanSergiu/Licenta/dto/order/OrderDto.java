package com.StefanSergiu.Licenta.dto.order;

import com.StefanSergiu.Licenta.dto.orderDetail.OrderDetailDto;
import com.StefanSergiu.Licenta.dto.product.PlainProductDto;
import com.StefanSergiu.Licenta.entity.OrderDetail;
import com.StefanSergiu.Licenta.entity.Orders;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private Long id;
    private Integer userId;
    private Float total;
    private LocalDateTime generationDateTime;
    private String status;
    private List<OrderDetailDto> orderDetails;
    private String deliveryAddress;
    private String paymentMethod;
    private String billingName;
    private String contactPhone;

    public static OrderDto from(Orders order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setTotal(order.getTotal());
        orderDto.setGenerationDateTime(order.getGenerationDateTime());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDetails(order.getOrderDetails().stream().map(OrderDetailDto::from).collect(Collectors.toList()));
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setBillingName(order.getBillingName());
        orderDto.setContactPhone(order.getContactPhone());
        return orderDto;
    }
}
