package com.osp.bttp.dao.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * Created by Admin on 12/27/2017.
 */
@Data
public class CreateGroupRequest {
    @Schema(
            title = "Mã nhóm",
            name = "id",
            type = "Long",
            example = "1")
    private Long id;

    @Schema(
            title = "Tên nhóm",
            name = "groupName",
            type = "String",
            example = "Nhóm 1")
    @NotNull(message = "Tên nhóm không được null")
    @NotEmpty(message = "Tên nhóm không được rỗng")
    private String groupName;

    @Schema(
            title = "Mô tả",
            name = "description",
            type = "String",
            example = "Mô tả nhóm 1")
//    @NotNull
//    @NotEmpty
    private String description;

    @Schema(
            title = "Danh sách quyền",
            name = "listAuthority",
            type = "String",
            example = "1,2,3")
    @NotNull
    @NotEmpty
    private String listAuthority;

    @Schema(
            title = "Có phải nhóm mặc định. 1: là nhóm mặc định, 0: không phải nhóm mặc định",
            name = "isDefault",
            type = "Long",
            example = "1", required = true)
    @NotNull(message = "Có phải nhóm mặc định không được null")
    private Long isDefault;

    //nhóm đối tượng
    @Schema(
            title = "Nhóm đối tượng",
            name = "type",
            type = "Long",
            example = "1")
    @NotNull
    private Long type;


}
