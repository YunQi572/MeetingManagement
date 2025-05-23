package com.example.meetingmanage.entities;

import java.util.HashMap;
import java.util.Map;

public class Msg {
    private int code;
    private String message;
    private Map<String, Object> extend;

    public Msg add(Map<String, Object> extend) {
        this.extend = extend;
        return this;
    }

    /**
     * Helper method to add token directly
     * 
     * @param token The JWT token
     * @return this Msg instance for method chaining
     */
    public Msg addToken(String token) {
        if (this.extend == null) {
            this.extend = new HashMap<>();
        }
        this.extend.put("token", token);
        return this;
    }

    public static Msg success() {
        Msg msg = new Msg();
        msg.setCode(100);
        msg.setMessage("操作成功");
        return msg;
    }

    public Msg setError(String message) {
        setMessage(message);
        return this;
    }

    public static Msg fault() {
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMessage("操作失败");
        return msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
