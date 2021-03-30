package com.example.demo_gyroscope;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo_gyroscope.server.OrientationData;

import net.jodah.concurrentunit.Waiter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@SpringBootTest
class DemoGyroscopeApplicationTests {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoGyroscopeApplicationTests.class);
    final WebClient client = WebClient.builder().baseUrl("http://127.0.0.1:80").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	@Test
	public void testSendOrientation() throws TimeoutException {
        Hooks.onErrorDropped(error -> {
            LOGGER.warn("An error has occurred", error);
        });
		final Waiter waiter = new Waiter();
		OrientationData data1 = new OrientationData(1,1,1);
		OrientationData data2 = new OrientationData(1,1,1);
		client.post().uri("/orientation").body(Mono.just(data1), OrientationData.class).retrieve().bodyToMono(OrientationData.class);
		LOGGER.info("data sent: {}", data1);
		client.post().uri("/orientation").body(Mono.just(data2), OrientationData.class).retrieve().bodyToMono(OrientationData.class);
		LOGGER.info("data sent: {}", data2);
		Flux<OrientationData> response = client.get().uri("/orientation").retrieve().bodyToFlux(OrientationData.class);
		response.subscribe(average -> {
			LOGGER.info("data received: {}", average.toString());
		});
	}

}
