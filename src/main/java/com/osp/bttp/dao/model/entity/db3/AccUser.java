package com.osp.bttp.dao.model.entity.db3;


import com.osp.bttp.common.utils.DateUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.entity.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "acc_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccUser extends BaseModel implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_USER_SEQ")
    @SequenceGenerator(sequenceName = "ACC_USER_SEQ", allocationSize = 1, name = "ACC_USER_SEQ")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PWD")
    private String password;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "STATUS")
    private Integer status;//Trạng thái (0: đã khóa, 1: hoạt động, 2: xóa)

    @Column(name = "TYPE")
    @Schema(description = "Loại người dùng (45: quản trị viên, BO_TU_PHAP 40, SO_TU_PHAP 30)")
    private Integer type;

    @Column(name = "GENDER")
    private Integer gender;

    @Column(name = "DATE_BIRTH")
    private Date dateBirth;

    @Column(name = "ID_NUMBER")
    private String passportId;

    @Column(name = "IDENTITY_DATE")
    private Date identityDate;

    @Column(name = "IDENTITY_PLACE")
    private String identityPlace;

    @Column(name = "HOME_TOWN")
    private String homeTown;

    @Column(name = "PLACE_RESIDENCE")
    private String residencePlace;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "COMMUNE_ID")
    private Integer communeId;


    @Column(name = "JWT")
    private String jwt;

    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;

    @Transient
    private List<GrantedAuthority> grantedAuths;

    @Transient
    private String communeName;
    @Transient
    private String districtName;
    @Transient
    private String provinceName;

    @Transient
    private String identityDateStr;

    public String getIdentityDateStr() {
        if(H.isTrue(this.identityDate)) {
            return DateUtils.convertDateToStringWithType(this.identityDate, "dd/MM/yyyy");
        }
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuths;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }



}
