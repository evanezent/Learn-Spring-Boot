package com.example.learnspring.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearnController {
    @GetMapping(path = "/hell-oh")
    public String hellohWorld(){
        return "Hell Oh World";
    }
}
