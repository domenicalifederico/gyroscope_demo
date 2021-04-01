package com.example.demo_gyroscope;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo_gyroscope.server.OrientationData;
import com.example.demo_gyroscope.server.ReactorServer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoGyroscopeApplicationTests {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoGyroscopeApplicationTests.class);
    
	private WebTestClient webTestClient = WebTestClient.bindToController(ReactorServer.class).build();

	@Test
	public void testSendOrientation() throws TimeoutException, InterruptedException {
        Hooks.onErrorDropped(error -> {
            LOGGER.warn("An error has occured {}", error);
        });
		OrientationData data = new OrientationData(1,1,1);
		OrientationData data2 = new OrientationData(10,10,10);
		this.webTestClient.mutate().responseTimeout(Duration.ofMillis(30000));
		this.webTestClient.put().uri("/orientation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(Mono.just(data), OrientationData.class).exchange().expectStatus().isOk();
		this.webTestClient.put().uri("/orientation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(Mono.just(data2), OrientationData.class).exchange().expectStatus().isOk();
		Flux<OrientationData> response = this.webTestClient.get().uri("/orientation").exchange().returnResult(OrientationData.class).getResponseBody();
		response.subscribe( average -> LOGGER.info(response.next().block().toString()));
	}

}
