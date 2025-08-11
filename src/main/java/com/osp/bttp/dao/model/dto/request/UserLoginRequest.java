package com.osp.bttp.dao.model.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {


    private String username;

    private String password;
    private String cf_turnstile_response;


    public String getUsername() {
        return username != null ? username.trim() : null;
    }

    public String getPassword() {
        return password != null ? password.trim() : null;
    }
}
