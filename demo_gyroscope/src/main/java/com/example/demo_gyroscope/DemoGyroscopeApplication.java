package com.example.demo_gyroscope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo_gyroscope.server.OrientationData;
import com.example.demo_gyroscope.server.ReactorServer;

@SpringBootApplication
public class DemoGyroscopeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGyroscopeApplication.class, args);
	}
		

}
