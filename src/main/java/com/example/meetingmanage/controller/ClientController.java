package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Client login endpoint
     * 
     * @param loginParams Map containing username and password
     * @return Msg object with success/failure status and token if successful
     */
    @PostMapping("/login")
    public Msg login(@RequestBody Map<String, Object> loginParams) {
        return clientService.login(loginParams);
    }
}
