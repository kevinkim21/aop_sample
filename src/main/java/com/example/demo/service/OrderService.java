package com.example.demo.service;

import com.example.demo.annotation.Trace;
import com.example.demo.controller.OrderReq;
import com.example.demo.controller.OrderRes;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    @Trace
    public OrderRes order(OrderReq orderReq) {
        String orderKey = UUID.randomUUID().toString().replace("-", "");
        /*if(true) {
            throw new RuntimeException("test");
        }*/
        return new OrderRes(orderKey);
    }
}
