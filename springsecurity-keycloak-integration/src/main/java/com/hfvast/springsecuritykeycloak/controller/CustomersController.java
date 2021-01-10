package com.hfvast.springsecuritykeycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mawl
 */
@RestController
@RequestMapping("customers")
public class CustomersController {
    @GetMapping
    public String consumer(){
        return "customers";
    }
}
