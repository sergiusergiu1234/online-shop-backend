package com.StefanSergiu.Licenta.controller;

import com.StefanSergiu.Licenta.dto.order.CreateNewOrderModel;
import com.StefanSergiu.Licenta.dto.order.OrderDto;
import com.StefanSergiu.Licenta.dto.order.UpdateOrderModel;
import com.StefanSergiu.Licenta.entity.Orders;
import com.StefanSergiu.Licenta.entity.UserInfo;
import com.StefanSergiu.Licenta.service.OrderService;
import com.StefanSergiu.Licenta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateNewOrderModel createNewOrderModel){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();

        Orders order = orderService.createOrder(createNewOrderModel, userId);

        return new ResponseEntity<>(OrderDto.from(order), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping()
    public ResponseEntity<List<OrderDto>> getOrders(){
        //get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();
        UserInfo user = userService.getLoggedInUser(username);
        Integer userId = user.getId();

        List<Orders> orders = orderService.getOrdersByUser(userId);
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Orders order:orders){
            orderDtoList.add(OrderDto.from(order));
        }
        return new ResponseEntity<>(orderDtoList,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<OrderDto> updateOrderStatus(@RequestBody UpdateOrderModel updateOrderModel){
        Orders order = orderService.updateOrderStatus(updateOrderModel);
        return new ResponseEntity<>(OrderDto.from(order),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<Orders> orders = orderService.getAllOrders();
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Orders order:orders){
            orderDtoList.add(OrderDto.from(order));
        }
        return new ResponseEntity<>(orderDtoList,HttpStatus.OK);
    }
}
