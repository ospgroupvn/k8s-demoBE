package com.osp.bttp.dao.model.dto.db3;


import com.osp.bttp.dao.model.entity.db3.AccUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;


@Data
public class AccUserDTO {


    @Schema(description = "Tên đăng nhập")
    @NotNull(message = "Tên đăng nhập không được để trống")
    @NotEmpty(message = "Tên đăng nhập không được để trống")
    private String username;

    @Schema(description = "Mật khẩu")
    @NotNull(message = "Mật khẩu không được để trống")
    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;

    @Schema(description = "Họ và tên")
    @NotEmpty(message = "Họ và tên không được để trống")
    @NotNull(message = "Họ và tên không được để trống")
    private String fullName;

    @Schema(description = "Trạng thái (0: đã khóa, 1: hoạt động, 2: xóa)")
    private Integer status;

    @Schema(description = "Loại người dùng (45: quản trị viên, BO_TU_PHAP 40, SO_TU_PHAP 30)")
    @NotNull(message = "Loại người dùng không được để trống")
    private Integer type;

    @Schema(description = "Giới tính (0: Nam, 1: Nữ)")
    private Integer gender;

    @Schema(description = "Ngày sinh")
    private Date dateBirth;

    @Schema(description = "Số CMND")
    private String passportId;

    @Schema(description = "Ngày cấp CMND")
    private Date identityDate;

    @Schema(description = "Nơi cấp CMND")
    private String identityPlace;

    @Schema(description = "Quê quán")
    private String homeTown;

    @Schema(description = "Nơi thường trú")
    private String residencePlace;

    @Schema(description = "Số điện thoại")
    private String mobile;

    @Schema(description = "Địa chỉ")
    private String address;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Mã tỉnh")
    private Integer provinceId;

    @Schema(description = "Mã quận")
    private Integer districtId;

    @Schema(description = "Mã xã")
    private Integer communeId;

    @Column(name = "JWT")
    private String jwt;

    @Column(name = "ADMINISTRATION_ID")
    @Schema(description = "Mã đơn vị")
    private Long administrationId;


    public AccUser mapToEntity(AccUser user) {
        user.setUsername(this.username);
        user.setPassword(null);
        user.setFullName(this.fullName);
        user.setStatus(this.status);
        user.setType(this.type);
        user.setGender(this.gender);
        user.setDateBirth(this.dateBirth);
        user.setPassportId(this.passportId);
        user.setIdentityDate(this.identityDate);
        user.setIdentityPlace(this.identityPlace);
        user.setHomeTown(this.homeTown);
        user.setResidencePlace(this.residencePlace);
        user.setMobile(this.mobile);
        user.setAddress(this.address);
        user.setEmail(this.email);
        user.setProvinceId(this.provinceId);
        user.setDistrictId(this.districtId);
        user.setCommuneId(this.communeId);
        user.setJwt(this.jwt);
        user.setAdministrationId(this.administrationId);
        return user;
    }
}
