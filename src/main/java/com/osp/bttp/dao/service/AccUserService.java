package com.osp.bttp.dao.service;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.UserInfo;
import com.osp.bttp.dao.model.dto.db3.AccUserDTO;
import com.osp.bttp.dao.model.dto.request.AccountRegisterRequest;
import com.osp.bttp.dao.model.dto.request.UserLoginRequest;
import com.osp.bttp.dao.model.dto.response.UserInfoResponse;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * @author sangnk
 * @Created 08/10/2024 - 3:13 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface AccUserService {
    ResponseEntity<ApiResponseV1<UserInfo>> authenUser(UserLoginRequest loginRequest, HttpServletRequest request);

    AccUser findById(Long userId);

    ResponseEntity<ApiResponseV1<?>> register(AccountRegisterRequest req, HttpServletRequest request);

    ResponseEntity<ApiResponseV1<UserInfoResponse>> getUserInfo();


    Optional<AccUser> getByUsername(String trim);

    ApiResponseV1 addUser(AccUserDTO user);

    ResponseEntity<ApiResponseV1> testRedis();

    ResponseEntity<ApiResponseV1<PagingResult>> searchUser(int pageNumber, int numberPerPage, String username, String fullName, String type, String mobile, String email, String address);
}
