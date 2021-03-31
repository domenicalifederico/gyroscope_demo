package com.example.demo_gyroscope.server;

import java.time.Duration;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import net.jodah.concurrentunit.Waiter;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;



@RestController
public class ReactorServer {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReactorServer.class);

    private SubscribableChannel outDataChannel = MessageChannels.publishSubscribe().get();
    private SubscribableChannel inDataChannel = MessageChannels.publishSubscribe().get();
    
    public ReactorServer() {
    	
    }
    

    
    
    @PostConstruct
    public void init() throws InterruptedException {
    	Flux<OrientationData> flux = Flux.create(emitter -> {
    		inDataChannel.subscribe(msg -> emitter.next( OrientationData.class.cast( msg.getPayload() )));
    	});

    	flux = flux.buffer(Duration.ofSeconds(3)).map(OrientationData::average);

    	ConnectableFlux<OrientationData> hot = flux.publish();
    	
    	hot.subscribe(orientationData -> outDataChannel.send(new GenericMessage<>(orientationData)));
    	
    	hot.connect();
    	
    	System.out.println("server initalized");
 
    }
    
    @GetMapping(path = "/orientation", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrientationData> getOrientation() {

        LOG.info("get orientation - HTTP GET CALLED");
        System.out.println("get orientation data..");

        return Flux.create( sink -> {
            FluxSink<OrientationData> fsink = sink;
            MessageHandler handler = msg -> fsink.next(OrientationData.class.cast(msg.getPayload()));;
            outDataChannel.subscribe(handler);
        });

    }

    @PutMapping(path = "/orientation")
    public void addOrientation(@RequestBody OrientationData orientationData) {

        LOG.info("add orientation - HTTP PUT CALLED {}", orientationData);
        System.out.println("add orientation data..");

        inDataChannel.send(new GenericMessage<>(orientationData));

    }



    
    
    
    

}
