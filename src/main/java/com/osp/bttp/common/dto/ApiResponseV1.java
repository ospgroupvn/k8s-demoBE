package com.osp.bttp.common.dto;

import com.osp.bttp.common.exception.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ApiResponseV1<T> {
    @Schema(
            title = "Mã phản hồi",
            name = "success",
            type = "boolean",
            example = "true")
    private boolean success;
    @Schema(
            title = "Nội dung thông báo",
            name = "message",
            type = "String",
            example = "Thành công")
    private String message;
    @Schema(
            title = "Mã lỗi",
            name = "",
            type = "String",
            example = "1")
    private int code;

    private T data;

    private Long total;

    public ApiResponseV1() {
    }

    public ApiResponseV1(boolean success, String message, T data) {
        this.success = success;
        if(success){
            this.code = Result.SUCCESS.getCode();
        }else{
            this.code = Result.BAD_REQUEST.getCode();
        }
        this.message = message;
        this.data = data;
    }

    public ApiResponseV1(boolean success, String message, T data, Long total) {
        this.success = success;
        if(success){
            this.code = Result.SUCCESS.getCode();
        }else{
            this.code = Result.BAD_REQUEST.getCode();
        }
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public ApiResponseV1(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponseV1(ConstantResponse enumResp, T data) {
        this.success = enumResp.isSuccess();
        this.message = enumResp.getMessage();
        this.code = enumResp.getErrorCode();
        this.data = data;
    }

    public ApiResponseV1(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponseV1(Result result) {
        this.success = result.isSuccess();
        this.message = result.getMessage();
        this.code = result.getCode();
    }

    public ApiResponseV1(boolean success, Result result) {
        this.success = success;
        this.message = result.getMessage();
        this.code = result.getCode();
    }

    public ApiResponseV1(boolean success, Result result, T data) {
        this.success = success;
        this.message = result.getMessage();
        this.code = result.getCode();
        this.data = data;
    }

    public ApiResponseV1(boolean success, int code, Map<String, Object> body, Result result) {
        this.success = success;
        this.code = code;
        this.message = result.getMessage();
        this.data = (T) body;
    }

    public static ApiResponseV1 badRequest(String message) {
        return new ApiResponseV1<>(false, Result.BAD_REQUEST.getCode(), message, null);
    }

    public static ApiResponseV1 serverError(String message) {
        return new ApiResponseV1<>(false, Result.SERVER_ERROR.getCode(), message, null);
    }

    public static ApiResponseV1 ok() {
        return new ApiResponseV1<>(true, Result.SUCCESS.getCode(), Result.SUCCESS.getMessage(), null);
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
