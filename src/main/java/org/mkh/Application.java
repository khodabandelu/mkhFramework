package org.mkh;

import org.mkh.frm.config.FrmProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties({FrmProperties.class})
public class Application {

    @PostConstruct
    public void initApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}