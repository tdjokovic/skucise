package com.example.skucise.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    //ako dobijemo get zahtev home
    //moze valjda da pise i /home umesto samo /
    @GetMapping("/")
    public String hello(){
        return "Hello Tijanino blabla!";
    }

    @GetMapping("/hello2")
    public String hello2(){
        return "<h1>Hello!</h1>";
    }

    @GetMapping("/hello3")
    public String hello3(@RequestParam(name="ime", defaultValue = "world") String ime){
        //ako ima vise request parametara, samo se razdvoje zapetom
        return "<h1> HELLOOOOO  "+ ime +" ! </h1>";
    }


}
