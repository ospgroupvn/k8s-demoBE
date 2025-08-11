package com.osp.bttp.endpoint;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.db3.AccUserDTO;
import com.osp.bttp.dao.model.dto.response.UserInfoResponse;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.AccUserView;
import com.osp.bttp.dao.service.AccUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author sangnk
 * @Created 09/10/2024 - 9:50 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Slf4j
@RestController
@RequestMapping("/v1/api/user")
public class UserController {
    @Autowired
    private AccUserService accUserService;
    @GetMapping("/info")
    public ResponseEntity<ApiResponseV1<UserInfoResponse>> getUserInfo() {
        return accUserService.getUserInfo();
    }

    @Operation(summary = "Danh sách người dùng")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "1", description = "Thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Thất bại")
    })
    @GetMapping("/list")
    @Secured({ConstantAuthor.SYSTEM.system})
    public ResponseEntity<ApiResponseV1<PagingResult>> searchUser(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                                                  @RequestParam(value = "numberPerPage", required = false, defaultValue = "15") int numberPerPage,
                                                                  @RequestParam(value = "username", required = false, defaultValue = "") String username,
                                                                  @RequestParam(value = "fullName", required = false, defaultValue = "") String fullName,
                                                                  @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                                                  @RequestParam(value = "mobile", required = false, defaultValue = "") String mobile,
                                                                  @RequestParam(value = "email", required = false, defaultValue = "") String email,
                                                                  @RequestParam(value = "address", required = false, defaultValue = "") String address) {
        return accUserService.searchUser(pageNumber, numberPerPage, username, fullName, type, mobile, email, address);
    }


    @PostMapping("/add")
    @Secured({ConstantAuthor.USER.add, ConstantAuthor.SYSTEM.system})
    public ResponseEntity<ApiResponseV1<?>> UserAdd(@RequestBody @Valid AccUserDTO user, HttpServletRequest request) {
        try {
            if (user != null) {
                if (user.getUsername() == null || user.getUsername().length() == 0) {
                    return new ResponseEntity<>(new ApiResponseV1(false, 20, "Username không được để trống", null), HttpStatus.BAD_REQUEST);
                }
                if (!H.isTrue(user.getType())) {
                    return new ResponseEntity<>(new ApiResponseV1(false, 28, "Loại người dùng không hợp lệ", null), HttpStatus.BAD_REQUEST);
                }
                if (user.getFullName() == null || user.getFullName().length() == 0) {
                    return new ResponseEntity<>(new ApiResponseV1(false, 21, "Họ tên không được để trống", null), HttpStatus.BAD_REQUEST);
                }

                AccUser userCheck = accUserService.getByUsername(user.getUsername().trim()).orElse(null);
                if (userCheck != null) {
                    return new ResponseEntity<>(new ApiResponseV1(false, 23, "Tài khoản đã tồn tại. Vui lòng kiếm tra lại!", null), HttpStatus.BAD_REQUEST);
                }

                user.setFullName(user.getFullName().trim());
                try {
                    ApiResponseV1 checkAdd = accUserService.addUser(user);
                    if (checkAdd.isSuccess()) {
                        return new ResponseEntity<>(checkAdd, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(checkAdd, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return new ResponseEntity<>(new ApiResponseV1(false, 25, "Có lỗi xảy ra. Hãy thử lại sau!", null), HttpStatus.BAD_REQUEST);
                }

            } else {
                return new ResponseEntity<>(new ApiResponseV1(false, 27, "Có lỗi xảy ra. Hãy thử lại sau!", null), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1(false, 27, "Có lỗi xảy ra. Hãy thử lại sau!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/testRedis")
    public ResponseEntity<ApiResponseV1> testRedis() {
        return accUserService.testRedis();
    }
}
