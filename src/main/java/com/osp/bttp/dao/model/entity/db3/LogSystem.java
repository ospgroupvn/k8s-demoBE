package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "LOG_SYSTEM")
public class LogSystem extends BaseEntity {
    @Id
    @SequenceGenerator(name="LOG_SYSTEM_SEQ", sequenceName="LOG_SYSTEM_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_SYSTEM_SEQ")
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "OBJECT_NAME",length = 100)
    private String objectName;

    @Column(name = "OBJECT_ID",length = 100)
    private String objectId;

    @Column(name = "IP",length = 100)
    private String ip;

    @Column(name = "ACTIONS",length = 200)
    @Enumerated(EnumType.STRING)
    private ActionType actions;

    @Column(name = "GROUPS",length = 200)
    @Enumerated(EnumType.STRING)
    private GroupType groups;

    @Column(name = "ACTOR",length = 200)
    @Enumerated(EnumType.STRING)
    private ActorType actor;



}
