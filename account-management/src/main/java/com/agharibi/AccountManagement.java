package com.agharibi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AccountManagement {

    public static void main(String[] args) {
        SpringApplication.run(AccountManagement.class, args);
    }

}
