package com.wfp.lmmis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
//@SpringBootApplication()
@ComponentScan("com.wfp.*")
public class ImlmaApplication {

    public static void main(String[] args) {

        SpringApplication.run(ImlmaApplication.class, args);
    }

}
