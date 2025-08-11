package com.osp.bttp.dao.model.entity.db2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 12/26/2017.
 */
@Entity
@Table(name = "ADM_USERS")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = -8299255898396933698L;
    @Id
    @SequenceGenerator(name = "ADM_USERS_SEQ", sequenceName = "ADM_USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_USERS_SEQ")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

//    @NotNull
//    @NotEmpty
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String username;

//    @NotNull
//    @NotEmpty
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

//    @NotNull
//    @NotEmpty
    @Column(name = "FULL_NAME", nullable = false, length = 100)
    private String fullName;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Column(name = "LAST_ACCESS_TIME")
    private Date lastAccessTime;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "GEN_DATE")
    private Date genDate;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @Column(name = "TYPE")
    private Long type;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "IS_REVOKE")
    private Long isRevoke;

    @Column(name = "PASSWORD_CHANGED")
    private Long passWordChanged;

    @Column(name="IS_SOURCE_BLOCK")
    private Long isSourceBlock;
    private transient List<GrantedAuthority> grantedAuths;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuths;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != 2;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return true;
        return this.status == 1;
    }

    public List<GrantedAuthority> getGrantedAuths() {
        return grantedAuths;
    }

    public void setGrantedAuths(List<GrantedAuthority> grantedAuths) {
        this.grantedAuths = grantedAuths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getIsRevoke() {
        return isRevoke;
    }

    public void setIsRevoke(Long isRevoke) {
        this.isRevoke = isRevoke;
    }

    public Long getPassWordChanged() {
        return passWordChanged;
    }

    public void setPassWordChanged(Long passWordChanged) {
        this.passWordChanged = passWordChanged;
    }

    public Long getIsSourceBlock() {
        return isSourceBlock;
    }

    public void setIsSourceBlock(Long isSourceBlock) {
        this.isSourceBlock = isSourceBlock;
    }
}
