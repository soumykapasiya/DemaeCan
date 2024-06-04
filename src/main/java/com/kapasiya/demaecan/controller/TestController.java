package com.kapasiya.demaecan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController
{
    @GetMapping("/test")
    public String testController()
    {
        System.out.println("TestController");
        return "testController";
    }

}
