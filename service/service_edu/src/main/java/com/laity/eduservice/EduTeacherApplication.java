package com.laity.eduservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication   //启动类注解
@ComponentScan(basePackages = {"com.laity"}) //加入注解 要不然swaggerconfig 配置类找不到，因为是不同的项目，如果想在a中用b的配置就得修改扫描规则
public class EduTeacherApplication {
//http://localhost:8001/swagger-ui.html   。。。。swagger固定地址：swagger-ui.html
    //用main方法启动
    public static void main(String[] args) {
        SpringApplication.run(EduTeacherApplication.class, args);
    }
}
