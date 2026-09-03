package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.entity.User;
import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User userDetail) {
        return userRepository.save(userDetail);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {

        Optional<User> optionalUser =
                userRepository.findById(id);

        return getUser(optionalUser);
    }

    private static User getUser(Optional<User> optionalUser) {

        return optionalUser.orElseThrow(
                () -> new BusinessException(
                        "User Not Found",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{email}/")
    public User getUserByEmail(@PathVariable String email) {

        return getUser(
                userRepository.findByEmail(email)
        );
    }

    @PatchMapping("/{email}/")
    public User updateUser(
    @PathVariable String email,
    @RequestBody User userDetail) {

        User existUser =
        getUser(userRepository.findByEmail(email));

        if (userDetail.getPassword() != null) {

            existUser.setPassword(
                userDetail.getPassword()
            );
        }

        if (userDetail.getName() != null) {

            existUser.setName(
                userDetail.getName()
            );
        }

        return userRepository.save(existUser);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        User existUser =
                getUser(userRepository.findById(id));

        userRepository.delete(existUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User Deleted");
    }

    @DeleteMapping("/all/")
    public ResponseEntity<String> deleteAllUsers() {

        userRepository.deleteAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("All Users Deleted");
    }

    @GetMapping("/page/{pageNo}/")
    public ResponseEntity<String> getUsers(
            @PathVariable int pageNo) {

        Pageable pageable = Pageable
                .ofSize(10)
                .withPage(pageNo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Current Page replace by Pageable");
    }
}
