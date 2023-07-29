package com.tutorial.appdemo.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tutorial.appdemo.models.Product;
import com.tutorial.appdemo.repositories.ProductRepository;

@Configuration // Bean method to declare DB
public class Database {
    // logger = system.out.println
    private static final Logger logger = LoggerFactory.getLogger(Database.class); // Object -- call getLogger and pass className, It will layout specific this class's information

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {

        // DB is not data =>It will insert some data, if don't have table => create table
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


/*Now connect with mysql using JPA
        1. docker run = pull image and create container
        2. -d = deamon = run container as a background process
        3. -rm = remove after stop container

        4. -e = Evironment variables
        5. -p = map Port , "host's port" : "container's port"
        6. --volume = map volume(folder) => "host's volume" : "container's volume"
        7. mysql: latest (is version)
*/
/*
docker run -d --rm --name mysql-spring-boot-tutorial `
-e MYSQL_ROOT_PASSWORD=123456 `
-e MYSQL_USER=hoangnd `
-e MYSQL_PASSWORD=123456 `
-e MYSQL_DATABASE=test_db `
-p 3301: 3006 `
--volume mysql-spring-boot-tutorial-volume://var/Lib/mysql  `
mysql: latest

 */