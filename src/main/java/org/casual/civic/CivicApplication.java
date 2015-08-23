package org.casual.civic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CivicApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx = SpringApplication.run(Configuration.class, args);
    }
}
