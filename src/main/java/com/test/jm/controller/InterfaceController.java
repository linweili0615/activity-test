package com.test.jm.controller;

import com.test.jm.dto.test.InterfaceDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @PostMapping
    public String test_interface(@RequestBody InterfaceDTO interfaceDTO){
        return null;
    }


}
