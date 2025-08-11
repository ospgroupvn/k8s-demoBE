package com.osp.bttp.dao.model.entity.db4;

/**
 * @author sangnk
 * @Created 17/03/2025 - 11:35 AM
 * @project = bttp
 * @_ Mô tả:
 */
import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "LAWYER_UPDATE_INFO_HIS")
@Data
public class LLawyerUpdateInfoHis extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lawyer_id")
    private Long lawyerId;

    @Column(name = "title")
    private String title;

    @Column(name = "license_type")
    private Integer licenseType;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "DECISION_NUMBER")
    private String decisionNumber;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PRACTICE_FORM") //Hình thức hành nghe
    private Integer practiceForm;

    //noi hành nghe
    @Column(name = "PRACTICE_PLACE")
    private String practicePlace;

}