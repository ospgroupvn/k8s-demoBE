package com.osp.bttp.dao.model.dto;


import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.entity.db1.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CallAddNotary {
    private NotaryInfo notaryInfo;
    private DmDocument documentProbatio;
    private ProbationaryInfo probationaryInfo;

    /*thay đổi nơi tập sự*/
    private List<DocummentProbationary> documentChangePro;

    /*tạm ngừng tập sự*/
    private List<DocummentProbationary> documentPausePro;

    public CallAddNotary() {
    }

    public ApiResponseV1 validate(CallAddNotary call) {

        if(call.getNotaryInfo() == null || call.getDocumentProbatio() == null || call.getProbationaryInfo() == null){
            return new ApiResponseV1(false, 1,"Vui lòng nhập đầy đủ thông tin.", null);
        }
        NotaryInfo info = call.getNotaryInfo();
        DmDocument document = call.getDocumentProbatio();
        ProbationaryInfo pro = call.getProbationaryInfo();

        if(StringUtils.isBlank(info.getName())){
            return new ApiResponseV1(false,2,"Họ và tên không được để trống", null);
        }

        if(info.getBirthDay() == null){
            return new ApiResponseV1(false,3,"Ngày sinh không được để trống", null);
        }

        if(info.getSex() == null){
            return new ApiResponseV1(false,4,"Giới tính không được để trống", null);
        }

        if(StringUtils.isBlank(info.getIdNo()) ){
            return new ApiResponseV1(false,5,"Số CMND/ Hộ chiếu/ CCCD không được để trống", null);
        }

        /*if(info.getIdNoDate() == null){
            return new ApiReponse(false,6,"Ngày cấp CMND/ Hộ chiếu không được để trống");
        }*/

        /*if(StringUtils.isBlank(info.getPhoneNumber())){
            return new ApiReponse(false,7,"Số điện thoại không được để trống");
        }*/

        /*if(info.getAddressNowId() == null){
            return new ApiReponse(false,8,"Địa chỉ thường trú không được để trống");
        }*/

        if(info.getAddressResidentId() == null) {
            return new ApiResponseV1(false,9,"Địa chỉ tạm trú không được để trống", null);
        }

        if(StringUtils.isBlank(document.getDispatchCode())) {
            return new ApiResponseV1(false,10,"Tổ chức HNCC tập sự không được để trống", null);
        }

        if(document.getDateSign() == null) {
            return new ApiResponseV1(false,11,"Ngày cấp không được để trống", null);
        }

        if(pro.getDateStart() == null) {
            return new ApiResponseV1(false,12,"Ngày bắt đầu tập sự không được để trống", null);
        }

        /*if(StringUtils.isBlank(document.getLinkFile()) || StringUtils.isBlank(document.getFileName())) {
            return new ApiReponse(false,13,"File đính kèm không được để trống");
        }*/

        if(pro.getOrgNotaryInfoId() == null) {
            return new ApiResponseV1(false,14,"Tổ chức HNCC tập sự không được để trống", null);
        }

        /*if(info.getStatus() == null) {
            return new ApiReponse(false,15,"Trạng thái không được để trống");
        }*/



        return new ApiResponseV1(true,200,"Thành công", null);
    }

    public ApiResponseV1 validateUpdate(CallAddNotary call){

        if(call.getDocumentProbatio().getId() == null){
            return new ApiResponseV1(false, 16, "Vui lòng chọn hồ sơ tập sự cần cập nhật", null);
        }
        if(call.getNotaryInfo().getId() == null){
            return new ApiResponseV1(false, 17, "Vui lòng chọn thông tin tập sự cần cập nhật", null);
        }
        if(call.getProbationaryInfo().getId() == null){
            return new ApiResponseV1(false, 18, "Vui lòng chọn thông tin tập sự cần cập nhật", null);
        }
        return new ApiResponseV1(true, 200, "Thành công", null);
    }

    public NotaryInfo getNotaryInfo() {
        return notaryInfo;
    }

    public void setNotaryInfo(NotaryInfo notaryInfo) {
        this.notaryInfo = notaryInfo;
    }

    public DmDocument getDocumentProbatio() {
        return documentProbatio;
    }

    public void setDocumentProbatio(DmDocument documentProbatio) {
        this.documentProbatio = documentProbatio;
    }

    public ProbationaryInfo getProbationaryInfo() {
        return probationaryInfo;
    }

    public void setProbationaryInfo(ProbationaryInfo probationaryInfo) {
        this.probationaryInfo = probationaryInfo;
    }

    public List<DocummentProbationary> getDocumentChangePro() {
        return documentChangePro;
    }

    public void setDocumentChangePro(List<DocummentProbationary> documentChangePro) {
        this.documentChangePro = documentChangePro;
    }

    public List<DocummentProbationary> getDocumentPausePro() {
        return documentPausePro;
    }

    public void setDocumentPausePro(List<DocummentProbationary> documentPausePro) {
        this.documentPausePro = documentPausePro;
    }
}
