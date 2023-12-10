package com.example.setkabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class }) //убрать ексклюд
public class SetkaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SetkaBotApplication.class, args);
    }

}
