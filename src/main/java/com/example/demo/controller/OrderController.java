package com.example.demo.controller;

import com.example.demo.annotation.Trace;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Trace
    @PostMapping("/order")
    public OrderRes order(@RequestBody OrderReq orderReq) {
        return orderService.order(orderReq);
    }
}
