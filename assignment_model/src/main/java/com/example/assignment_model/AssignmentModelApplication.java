package com.example.assignment_model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AssignmentModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentModelApplication.class, args);
    }

}
