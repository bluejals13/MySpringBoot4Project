package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.entity.User;
import com.rookies6.myspringboot4project.repository.CustomerRepository;
import com.rookies6.myspringboot4project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserRepository userRepository;

    //Constructor Injection - Mock 객체 주입이 가능
//    public UserRestController(UserRepository userRepository) {
//        log.info("UserRepository 구현 클래스명 = {}", userRepository.getClass().getName());
//        this.userRepository = userRepository;
//    }

    @PostMapping
    public User createUser(@RequestBody User userDetail){
        return userRepository.save(userDetail);
    }


}