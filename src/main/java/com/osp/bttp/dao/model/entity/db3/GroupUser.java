package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Data
@Table(name = "acc_group_user")
@AllArgsConstructor
@Getter
@Setter
public class GroupUser extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_GROUP_USER_SEQ")
    @SequenceGenerator(sequenceName = "ACC_GROUP_USER_SEQ", allocationSize = 1, name = "ACC_GROUP_USER_SEQ")
    private Long id;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "USER_ID")
    private Long userId;


    @Column(name = "USER_TYPE")
    private String userType;

    //username
    @Column(name = "USER_NAME")
    private String username;

    public GroupUser() {
    }

    public GroupUser(Long groupId, Long userId, String createBy, Date genDate) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public GroupUser(Long groupId, Long userId, String username, String createBy, Date genDate) {
        this.groupId = groupId;
        this.userId = userId;
        this.username = username;
    }
}
