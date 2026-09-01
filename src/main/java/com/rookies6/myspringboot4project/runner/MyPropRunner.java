package com.rookies6.myspringboot4project.runner;

import com.rookies6.myspringboot4project.config.MyEnvironment;
import com.rookies6.myspringboot4project.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties myPropProperties;

    @Autowired
    private MyEnvironment myEnvironment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("================ MyPropRunner 실행 ================");
        logger.debug("@Value 로드 -> username: {}, port: {}", username, port);
        logger.info("MyPropProperties 주입 -> username: {}, port: {}", 
                myPropProperties.getUsername(), myPropProperties.getPort());
        logger.info("현재 활성화된 MyEnvironment Profile Bean: {}", myEnvironment);
        logger.info("==================================================");
    }
}