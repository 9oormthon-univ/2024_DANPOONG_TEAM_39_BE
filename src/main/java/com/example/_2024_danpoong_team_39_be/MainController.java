package com.example._2024_danpoong_team_39_be;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("")
    public String sayHello() {
        return "Hello Songil!";
    }
}

