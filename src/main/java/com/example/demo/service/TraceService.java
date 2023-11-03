package com.example.demo.service;

import com.example.demo.dto.TraceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TraceService {

    public void save(TraceDto trace) {
        log.info("====== TRACE ======");
        log.info("trace: {}", trace);
    }
}
