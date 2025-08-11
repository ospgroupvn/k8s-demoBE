package com.osp.bttp.dao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenInfo {

    private String accessToken;

    private String idToken;

    private String tokenType;

    private long expiresIn;

    private String refreshToken;

    private String scope;

}
