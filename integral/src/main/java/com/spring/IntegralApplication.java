package com.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.spring.persistence")
public class
IntegralApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegralApplication.class, args);
	}
}
