package com.osp.bttp.dao.model.dto.db3;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgNotaryInfoCreateDto {
    // common info
    @NotNull
    @Size(max = 250,message = "max length 250 !")
    private String name;

    private String address;

    private String tel;

    private String email;

    @NotNull
    private Long notaryIdOfficeChief;

    private Long addressId;

    private Long administrationId;

    private Long active;
    @NotNull
    private Long status;

}
