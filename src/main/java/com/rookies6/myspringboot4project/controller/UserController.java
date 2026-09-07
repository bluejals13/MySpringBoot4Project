package com.rookies6.myspringboot4project.controller;

import com.rookies6.myspringboot4project.entity.User;
import com.rookies6.myspringboot4project.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/thymeleaf")
    public String leaf(Model model) {
        model.addAttribute("name", "스프링부트");
        return "leaf";
    }

    // / 와 /index 둘 다 User List를 보여줌
    @GetMapping({"/", "/index"})
    public ModelAndView userList() {

        List<User> userList = userRepository.findAll();

        return new ModelAndView(
                "index",
                "users",
                userList
        );
    }

    @GetMapping("/signup")
    public String showSignUpForm(
            @ModelAttribute("userForm") User user) {

        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(
            @Valid @ModelAttribute("userForm") User user,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(user);

        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") long id,
            Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid user Id:" + id
                        ));

        model.addAttribute("userForm", user);

        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") long id,
            @Valid @ModelAttribute("userForm") User userForm,
            BindingResult result) {

        if (result.hasErrors()) {
            userForm.setId(id);
            return "update-user";
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid user Id:" + id
                        ));

        existingUser.setName(userForm.getName());
        existingUser.setEmail(userForm.getEmail());

        userRepository.save(existingUser);

        return "redirect:/index";
    }
}
