package com.osp.bttp.dao.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterResponse {

    private Long status;

    private String statusStr;

    private String username;

    public AccountRegisterResponse(String username) {
        this.username = username;
    }
}
