package com.example.demo.service;

import com.example.demo.dto.TraceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceService {
    private final Environment environment;
    private final KafkaTemplate<String,String> devKafkaTemplate;
    private final KafkaTemplate<String,String> prodKafkaTemplate;

    public void save(TraceDto trace) {
        log.info("====== TRACE ======");
        log.info("trace: {}", trace);
        this.getKafkaTemplate().send("trace", trace.toString());
    }

    private KafkaTemplate<String, String> getKafkaTemplate() {
        // 현재 활성화된 프로필들을 가져옵니다.
        String[] activeProfiles = environment.getActiveProfiles();
        // 활성화된 프로필에 따라 적절한 KafkaTemplate을 선택합니다.
        // 예를 들어, "dev" 또는 "prod" 프로필을 확인하고 해당하는 KafkaTemplate을 반환합니다.
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase("dev")) {
                return devKafkaTemplate;
            }
            if(profile.equalsIgnoreCase("prod")) {
                return prodKafkaTemplate;
            }
        }
        return prodKafkaTemplate;
    }
}
