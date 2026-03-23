package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.OrderResponse;
import org.example.ecommers.service.OrderService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
       List<OrderResponse> orderResponseList= orderService.getAllOrders();
       return ResponseEntity.ok(orderResponseList);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long orderId){
        orderService.cancelOrder(orderId,null,true);

        return ResponseEntity.noContent().build();
    }






}
