
package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "acc_group_authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupAuthority extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_GROUP_AUTHORITY_SEQ")
    @SequenceGenerator(sequenceName = "ACC_GROUP_AUTHORITY_SEQ", allocationSize = 1, name = "ACC_GROUP_AUTHORITY_SEQ")
    private Long id;

    @Column(name = "GROUP_ID", nullable = false)
    private long groupId;

    @Column(name = "AUTHORITY", nullable = false)
    private long authority;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "GEN_DATE",nullable = false)
    private Date genDate;

    public GroupAuthority(Long groupId, Long authority, String username, Date date) {
        super();
        this.groupId = groupId;
        this.authority = authority;
        this.createBy = username;
        this.genDate = date;
        this.setLastUpdated(new Date());
    }
}
