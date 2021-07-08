package com.sparta.w1homework;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class W1HomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(W1HomeworkApplication.class, args);
    }

    // 서버시간 오류 해결!!!
    @PostConstruct
    public void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
