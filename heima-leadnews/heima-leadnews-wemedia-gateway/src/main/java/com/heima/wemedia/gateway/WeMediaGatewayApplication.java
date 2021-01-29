package com.heima.wemedia.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cuichacha
 * @date 1/28/21 21:00
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WeMediaGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeMediaGatewayApplication.class, args);
    }
}
