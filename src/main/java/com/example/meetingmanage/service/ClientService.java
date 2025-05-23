package com.example.meetingmanage.service;

import com.example.meetingmanage.entities.Client;
import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.mapper.ClientMapper;
import com.example.meetingmanage.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientService {

    private static final Logger logger = Logger.getLogger(ClientService.class);

    @Autowired
    private ClientMapper clientMapper;

    /**
     * Client login service
     * 
     * @param loginParams Map containing username and password
     * @return Msg object with success/failure status and token if successful
     */
    public Msg login(Map<String, Object> loginParams) {
        String username = (String) loginParams.get("username");
        String password = (String) loginParams.get("password");

        try {
            logger.info("Client login attempt - Username: " + username);

            // Find client by username and password
            Client client = clientMapper.findByUsernameAndPassword(username, password);

            if (client != null) {
                logger.info("Client [" + username + "] login successful");

                // Generate JWT token
                String token = TokenUtil.sign(client);

                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                data.put("uid", client.getUid());
                data.put("username", client.getUsername());

                return Msg.success().add(data);
            } else {
                logger.warn("Client [" + username + "] login failed, invalid credentials");
                return Msg.fault();
            }
        } catch (Exception e) {
            logger.error("Client login error: " + e.getMessage(), e);
            return Msg.fault().setError("Login processing error");
        }
    }
}