package com.laciesbox;

import com.laciesbox.domain.Chara;
import com.laciesbox.domain.User;
import com.laciesbox.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(UserRepository repository) {
        return (args) -> {
            // save a couple of users
            repository.save(new User("Dana Villareal"));
            repository.save(new User("Jayjay Cayabyab"));
            repository.save(new User("Brent Anonas"));
            repository.save(new User("Cobi Bautista"));
            repository.save(new User("Miwa Hanazawa"));
            repository.save(new User("Hannah Nolasco"));

            // fetch all users
            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            repository.findById(1L)
                    .ifPresent(user -> {
                        log.info("Userfound with findById(1L):");
                        log.info("--------------------------------");
                        log.info(user.toString());
                        log.info("");
                    });

            // fetch customers by last name
            log.info("Customer found with findByLastName('%a%'):");
            log.info("--------------------------------------------");
            repository.findByNameContainingIgnoreCase("a").forEach(nameWithA -> {
                log.info(nameWithA.toString());
            });
            log.info("");
        };
    }
}
