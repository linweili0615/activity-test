package com.test.jm.controller;

import com.test.jm.dto.test.InterfaceDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @PostMapping("/test")
    public Map<String,String> test_interface(@RequestBody InterfaceDTO interfaceDTO){
        System.out.println(interfaceDTO.toString());
        Map<String,String> map = new HashMap<String,String>();
        map.put("status","200");
        map.put("data","挺好");
        return map;
    }


}
