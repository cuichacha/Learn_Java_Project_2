package com.heima.wemedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author cuichacha
 * @date 1/27/21 18:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@ServletComponentScan
public class WeMediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeMediaApplication.class, args);
    }
}
