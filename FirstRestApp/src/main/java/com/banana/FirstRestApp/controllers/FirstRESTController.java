package com.banana.FirstRestApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // что бы не писать @ResponseBody над каждым методом
//@Controller
@RequestMapping("/api")
public class FirstRESTController {

//    @ResponseBody // возвращаем данные, а не ссылку на страницу представления
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello REST world!";
    }
}
