package com.osp.bttp.dao.model.dto;

//import com.osp.chonso.dao.model.entity.AdmRight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String userName;
    private AccessTokenInfo accessTokenInfo;
    private List<String> authorities;
    private String[] enablePayment;
    private Date lastChangePassword;
    private String source;
}
