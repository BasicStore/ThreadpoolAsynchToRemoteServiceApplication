package com.asynch;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.asynch.dm.User;
import com.asynch.services.GitHubLookupService;
import com.asynch.services.SheepService;


@SpringBootApplication
@EnableAsync
public class ThreadpoolAsynchToRemoteServiceApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ThreadpoolAsynchToRemoteServiceApplication.class);
	
	@Autowired
	private GitHubLookupService gitHubLookupService;
		
	@Autowired
	private SheepService sheepService;
	
	public static void main(String[] args) {
		logger.info("starting the app....");
		SpringApplication.run(ThreadpoolAsynchToRemoteServiceApplication.class, args);
	}

	
	@Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("GithubAndSheepLookup-");
        executor.initialize();
        return executor;
    }
	
	
	@Override
    public void run(String... args) throws Exception {
		
		// Start the clock
        long start = System.currentTimeMillis();
        
        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");

        // Wait until they are all done - the join() method does just that, or it throws a Completion or Cancellation exception
        // WHY WOULD YOU DO THIS?? defeats the object to wait until they all complete..... 
        CompletableFuture.allOf(page1,page2,page3).join();

        // Print results, including elapsed time
        logger.info("Elapsed time for processes: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());
        
        // ======================================================================================
        
        // invoke the BasicWebService running on this same machine
        // note that the same task executor is shared for this purpose
		logger.info("About to lookup sheep....");
		
		CompletableFuture<String> sheepGreeting1 = sheepService.breedSheep(1l);
		CompletableFuture<String> sheepGreeting2 = sheepService.breedSheep(2l);
		CompletableFuture<String> sheepGreeting3 = sheepService.breedSheep(3l);
		CompletableFuture<String> sheepGreeting4 = sheepService.breedSheep(4l);
		CompletableFuture<String> sheepGreeting5 = sheepService.breedSheep(5l);
		CompletableFuture<String> sheepGreeting6 = sheepService.breedSheep(6l);
		CompletableFuture<String> sheepGreeting7 = sheepService.breedSheep(7l);
		CompletableFuture<String> sheepGreeting8 = sheepService.breedSheep(8l);
		CompletableFuture<String> sheepGreeting9 = sheepService.breedSheep(9l);
		CompletableFuture<String> sheepGreeting10 = sheepService.breedSheep(10l);
		CompletableFuture<String> sheepGreeting11 = sheepService.breedSheep(11l);
		CompletableFuture<String> sheepGreeting12 = sheepService.breedSheep(12l);
		CompletableFuture.allOf(sheepGreeting1, sheepGreeting1, sheepGreeting1, sheepGreeting1, sheepGreeting1,
				sheepGreeting1, sheepGreeting1, sheepGreeting1, sheepGreeting1, sheepGreeting1,
				sheepGreeting1, sheepGreeting1).join();
		logger.info("Elapsed time for processes: " + (System.currentTimeMillis() - start));
        logger.info("--> " + sheepGreeting1.get());
        logger.info("--> " + sheepGreeting2.get());
        logger.info("--> " + sheepGreeting3.get());
        logger.info("--> " + sheepGreeting4.get());
        logger.info("--> " + sheepGreeting5.get());
        logger.info("--> " + sheepGreeting6.get());
        logger.info("--> " + sheepGreeting7.get());
        logger.info("--> " + sheepGreeting8.get());
        logger.info("--> " + sheepGreeting9.get());
        logger.info("--> " + sheepGreeting10.get());
        logger.info("--> " + sheepGreeting11.get());
        logger.info("--> " + sheepGreeting12.get());
    }
	
	
	
}
