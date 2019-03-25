package com.asynch.services;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.asynch.dm.User;


@Service
public class SheepService {

	private static final Logger logger = LoggerFactory.getLogger(SheepService.class);

    private final RestTemplate restTemplate;

    public SheepService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
	
	
    // creates a new sheep and returns an appropriate greeting for it
    @Async
    public CompletableFuture<String> breedSheep(long id) throws InterruptedException {
    	// localhost:8080/greeting?name=blar
    	String url = "http://localhost:8081/greeting?name=blarblarZZ" + id;
        String sheepGreeting = restTemplate.getForObject(url, String.class);
        return CompletableFuture.completedFuture(sheepGreeting);
    }
    
    
}
