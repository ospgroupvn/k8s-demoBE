package com.osp.bttp.dao.model.dto.db3;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaryInfoCreateDto {
    @NotNull
    @Size(max = 250,message = "length max 250!")
    private String name;
    private String email;
    private Long sex;
    private Date birthDay;
    @NotNull
    private String idNo;
    private Date idNoDate;
    private String addressIdNo;
    private String addressResident;
    private Long addressResidentId;
    private String addressNow;
    private Long addressNowId;
    @NotNull
    @Min(value = 0 ,message = "Trạng thái k hợp lệ")
    private Long status;
    private String phoneNumber;

    private Long administrationId;

}
