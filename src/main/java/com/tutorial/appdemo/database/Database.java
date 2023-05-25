package com.tutorial.appdemo.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tutorial.appdemo.models.Product;
import com.tutorial.appdemo.repositories.ProductRepository;
/*Now connect with mysql using JPA
 
*/

@Configuration // Bean method to declare DB
public class Database {
    // logger = system.out.println
    private static final Logger logger = LoggerFactory.getLogger(Database.class); // Object -- call getLogger and
    // pass className, It will layout
    // specific this class's
    // information

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {

        // DB is not data =>It will insert some data, if don't have table => create
        // table
        return new CommandLineRunner() { // Create object execute interface CommandLineRunner (specific is run function)
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Macbook Pro 16", 2020, 2400.0, "");
                Product productB = new Product("iPad Air Grean", 2021, 599.0, "");
                Product productC = new Product("IWatch Pro Black", 2022, 350.0, "");
                // Save 2 table into Database
                logger.info("Insert data: " + productRepository.save(productA)); // logger.info = system.out.print
                logger.info("Insert data: " + productRepository.save(productB));
                logger.info("Insert data: " + productRepository.save(productC));

            }
        };
    }
}
