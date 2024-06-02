package com.kapasiya.demaecan.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
{
    @GetMapping
    public String homeController()
    {
        System.out.println("Home Controller");
        return "Lomda.........";
    }
}
