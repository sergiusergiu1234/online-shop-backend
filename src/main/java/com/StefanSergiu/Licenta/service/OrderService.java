package com.StefanSergiu.Licenta.service;

import com.StefanSergiu.Licenta.dto.order.CreateNewOrderModel;
import com.StefanSergiu.Licenta.dto.order.UpdateOrderModel;
import com.StefanSergiu.Licenta.entity.*;
import com.StefanSergiu.Licenta.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;
    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Transactional
    public Orders getOrder(Long id){
        return orderRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Order with id "+id + " not found!"));
    }

    @Transactional
    public Orders createOrder(CreateNewOrderModel createNewOrderModel,Integer userId){
        //get user info
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User with id "+userId+" not found"));


        //generate new order object
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setGenerationDateTime(LocalDateTime.now());
        newOrder.setStatus("pending");

        newOrder.setBillingName(createNewOrderModel.getBillingName());
        newOrder.setPaymentMethod(createNewOrderModel.getPaymentMethod());
        newOrder.setDeliveryAddress(createNewOrderModel.getDeliveryAddress());
        newOrder.setContactPhone(createNewOrderModel.getContactPhone());

        newOrder.setTotal((float) 0);
        Float total = newOrder.getTotal();
        //TODO: get  all order details and add their costs and save it to price field
        //get empty orderDetails list
        List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailRepository.findByOrderId(newOrder.getId());
        //get user's shopping carts
        List<ShoppingCart> shoppingCarts = (List<ShoppingCart>) shoppingCartRepository.findByUserId(userId);

        for(ShoppingCart shoppingCart : shoppingCarts){



            //generate empty orderDetail and populate it based on shopping cart info
            OrderDetail newOrderDetail = new OrderDetail();
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProductName(shoppingCart.getProduct().getName());
            newOrderDetail.setOrderItem(newOrderItem);
            newOrderDetail.setQuantity(shoppingCart.getQuantity());
            newOrderDetail.setPrice(shoppingCart.getPrice());
          //  newOrderDetail.setSize(shoppingCart.getProduct().getSize());
            newOrder.setTotal(newOrder.getTotal()+ newOrderDetail.getPrice());

            productService.decreaseStock(shoppingCart.getQuantity(),shoppingCart.getProduct().getId());
            //set Order
            newOrderDetail.setOrder(newOrder);
            orderDetailRepository.save(newOrderDetail);
            //add it to Order's orderDetails list
            orderDetails.add(newOrderDetail);
            shoppingCartRepository.delete(shoppingCart);
            orderItemRepository.save(newOrderItem);
        }

        return orderRepository.save(newOrder);
    }

    public List<Orders> getOrdersByUser(Integer userId){
        return StreamSupport.stream(orderRepository.findByUserId(userId).spliterator(),false)
                .collect(Collectors.toList());
    }

    @Transactional
    public Orders updateOrderStatus(UpdateOrderModel updateOrderModel){
        Orders order= orderRepository.findById(updateOrderModel.getOrderId())
                .orElseThrow(()->new EntityNotFoundException("Order with id" + updateOrderModel.getOrderId()+ " doesn;t exist|!"));
        order.setStatus(updateOrderModel.getNewStatus());
        return order;
    }

    public List<Orders> getAllOrders(){
        return StreamSupport.stream(orderRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }
}
