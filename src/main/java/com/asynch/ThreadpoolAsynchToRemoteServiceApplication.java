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


@SpringBootApplication
@EnableAsync
public class ThreadpoolAsynchToRemoteServiceApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ThreadpoolAsynchToRemoteServiceApplication.class);
	
	@Autowired
	private GitHubLookupService gitHubLookupService;
	
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
        executor.setThreadNamePrefix("GithubLookup-");
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
    }
	
	
	
}
