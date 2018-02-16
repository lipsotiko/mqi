package io.egia.mqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws MqiExceptions {
    	
    	SpringApplication.run(Application.class, args);
		
    }
}