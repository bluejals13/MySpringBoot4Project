package com.rookies6.myspringboot4project.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class CustomVO {
    private String name;
    private int age;
}