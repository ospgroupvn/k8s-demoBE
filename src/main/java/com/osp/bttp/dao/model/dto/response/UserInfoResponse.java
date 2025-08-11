package com.osp.bttp.dao.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

	private Long id;


	private Integer status;

	private String username;
	private String fullName;

	private List<String> listAuthority;
	private String genderStr;

	private String birthday;

	private String passportId;

	private String identityDate;

	private String address;

	private String genDate;

	private Integer type;
	private Long administrationId;

	public UserInfoResponse(AccUser user, boolean isFindByRefCode) {
		this.fullName = user.getFullName();
		this.status = user.getStatus();
	}


}
