package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author sangnk
 * @Created 25/10/2024 - 11:25 SA
 * @project = bttp
 * @_ Mô tả:
 */

@Entity
@Data
@Table(name = "TCCC_NOTARY_ACTIVITY")
@AllArgsConstructor
@Getter
@Setter
public class TcccNotaryActivity extends BaseModel {
    /*
    CREATE TABLE NOTARY_ACTIVITY (
    ID NUMBER PRIMARY KEY,
    ADMINISTRATION_ID NUMBER ,
    REPORT_YEAR NUMBER NOT NULL,
    REPORT_MONTH NUMBER NOT NULL,
    NUM_NOTARY_CONTRACTS NUMBER NOT NULL, -- Số công việc công chứng hợp đồng, giao dịch
    NOTARY_FEES_CONTRACTS NUMBER NOT NULL, -- Thù lao công chứng hợp đồng, giao dịch
    NUM_OTHER_NOTARY_TASKS NUMBER NOT NULL, -- Số công việc công chứng bản dịch và các loại khác
    NOTARY_FEES_OTHER_TASKS NUMBER NOT NULL, -- Thù lao công chứng bản dịch và các loại khác
    TOTAL_TASKS NUMBER, -- Tổng số công việc
    TOTAL_FEES NUMBER, -- Tổng thù lao
    TAX_CONTRIBUTION NUMBER, -- Trích nộp ngân sách, thuế
    NUM_NOTARY_OFFICES_REPORTING NUMBER, -- Số tổ chức hành nghề CC có báo cáo
    NUM_NOTARIES_WORKING NUMBER -- Số công chứng viên
);

     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCCC_NOTARY_ACTIVITY_SEQ")
    @SequenceGenerator(sequenceName = "TCCC_NOTARY_ACTIVITY_SEQ", allocationSize = 1, name = "TCCC_NOTARY_ACTIVITY_SEQ")
    private Long id;

    @Column(name = "ADMINISTRATION_ID")
    @Schema(description = "ID cơ quan quản lý", example = "1")
    private Long administrationId;

    @Column(name = "REPORT_YEAR")
    @Schema(description = "Năm báo cáo", example = "2021")
    private Long reportYear;

    @Column(name = "REPORT_MONTH")
    @Schema(description = "Tháng báo cáo", example = "1")
    private Long reportMonth;

    @Column(name = "REPORT_DATE")
    @Schema(description = "Ngày báo cáo")
    private Date reportDate;

    @Column(name = "NUM_NOTARY_CONTRACTS")
    @Schema(description = "Số công việc công chứng hợp đồng, giao dịch", example = "100")
    private Long numNotaryContracts;

    @Column(name = "NOTARY_FEES_CONTRACTS")
    @Schema(description = "Thù lao công chứng hợp đồng, giao dịch", example = "1000000")
    private Long notaryFeesContracts;

    @Column(name = "NUM_OTHER_NOTARY_TASKS")
    @Schema(description = "Số công việc công chứng bản dịch và các loại khác", example = "100")
    private Long numOtherNotaryTasks;

    @Column(name = "NOTARY_FEES_OTHER_TASKS")
    @Schema(description = "Thù lao công chứng bản dịch và các loại khác", example = "1000000")
    private Long notaryFeesOtherTasks;

    @Column(name = "TOTAL_TASKS")
    @Schema(description = "Tổng số công việc", example = "200")
    private Long totalTasks;

    @Column(name = "TOTAL_FEES")
    @Schema(description = "Tổng thù lao", example = "2000000")
    private Long totalFees;

    @Column(name = "TAX_CONTRIBUTION")
    @Schema(description = "Trích nộp ngân sách, thuế", example = "200000")
    private Long taxContribution;

    @Column(name = "NUM_NOTARY_OFFICES_REPORTING")
    @Schema(description = "Số tổ chức hành nghề CC có báo cáo", example = "10")
    private Long numNotaryOfficesReporting;

    @Column(name = "NUM_NOTARIES_WORKING")
    @Schema(description = "Số công chứng viên", example = "100")
    private Long numNotariesWorking;

    public TcccNotaryActivity() {

    }
}
