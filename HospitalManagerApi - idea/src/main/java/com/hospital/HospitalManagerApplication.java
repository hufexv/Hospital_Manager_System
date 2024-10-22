package com.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//HospitalManagerApplication类是医院管理系统的启动类，通过注解配置和扫描，简化了应用的设置和启动过程，使得整个应用能够快速启动并准备好处理请求。

@MapperScan("com.hospital.mapper")
@SpringBootApplication
public class HospitalManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalManagerApplication.class, args);
    }

}
