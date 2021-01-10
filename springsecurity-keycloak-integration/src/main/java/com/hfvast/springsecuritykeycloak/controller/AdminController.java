package com.hfvast.springsecuritykeycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mawl
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    @GetMapping
    public String admin(){
        return "admin";
    }
}
