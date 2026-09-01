package com.rookies6.myspringboot4project.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my.boot")
@Getter @Setter
public class MyBootProperties {
    private String username;
    private int port;
    private String description;
}