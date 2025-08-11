package com.osp.bttp.endpoint;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.exception.Result;
import com.osp.bttp.dao.model.dto.AccessTokenInfo;
import com.osp.bttp.dao.model.dto.UserInfo;
import com.osp.bttp.dao.model.dto.request.AccountRegisterRequest;
import com.osp.bttp.dao.model.dto.request.UserLoginRequest;
import com.osp.bttp.dao.model.dto.response.AccountRegisterResponse;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.service.AccUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sangnk
 * @Created 09/10/2024 - 8:40 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Slf4j
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    @Autowired
    private AccUserService accUserService;

    @Operation(summary = "Api đăng nhập 1", description = "Api đăng nhập 2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "501", description = "Sai tên đăng nhập hoặc mật khẩu"),
            @ApiResponse(responseCode = "10", description = "Tham số không hợp lệ"),
            @ApiResponse(responseCode = "215", description = "Cần đổi mật khẩu"),
            @ApiResponse(responseCode = "510", description = "Tài khoản bị khóa"),
            @ApiResponse(responseCode = "511", description = "Tài khoản chưa xác thực"),
            @ApiResponse(responseCode = "502", description = "Tài khoản không tôn tại"),
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseV1<UserInfo>> authenUser(@Valid @RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        UserInfo userInfo = null;
        List<String> listAuthor = new ArrayList<>();
        Integer responseCode = 200;
        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, Result.INVALID_PARAM), HttpStatus.OK);
            }
            if (loginRequest.getUsername().charAt(1) == ' ') {
                String username = loginRequest.getUsername().substring(1).trim();
                loginRequest.setUsername(username.charAt(0) == '0' ? username : "0" + username);
            }
            return accUserService.authenUser(loginRequest, request);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponseV1<>(Result.SERVER_ERROR), HttpStatus.OK);
        }
    }


    @Operation(summary = "Api đăng ký tài khoản ctv")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "21", description = "Tài khoản đã tồn tại")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseV1<?>> register(HttpServletRequest request,
                                                                          @Valid @RequestBody AccountRegisterRequest req) {
        try {

            if (req.getUsername() == null ) {
                return new ResponseEntity<>(new ApiResponseV1<>(Result.INVALID_PARAM_MSISDN), HttpStatus.OK);
            }
            if (req.getPassword() == null || req.getPassword().isEmpty() || req.getPassword().trim().length() >= 20) {
                return new ResponseEntity<>(new ApiResponseV1<>(Result.INVALID_PARAM_PASSWORD), HttpStatus.OK);
            }
            return accUserService.register(req, request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponseV1<>(Result.SERVER_ERROR), HttpStatus.OK);
        }
    }
}
