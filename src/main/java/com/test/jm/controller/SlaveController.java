package com.test.jm.controller;

import com.test.jm.dto.Person;
import com.test.jm.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlaveController {

    @Autowired
    private SlaveService slaveService;

    @GetMapping("/slave")
    public Person getPerson(){
        return slaveService.getPerson();
    }

}
