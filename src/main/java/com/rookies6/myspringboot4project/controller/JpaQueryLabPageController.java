package com.rookies6.myspringboot4project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JpaQueryLabPageController {

    @GetMapping("/jpa-query-lab")
    public String jpaQueryLab() {
        return "jpa-query-lab";
    }
}
