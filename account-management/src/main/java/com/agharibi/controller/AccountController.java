package com.agharibi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("accounts")
@RestController
public class AccountController {

    @GetMapping("status")
    public String status() {
        return "Account management is up & running";
    }
}
