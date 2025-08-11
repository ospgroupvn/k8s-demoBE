
package com.osp.bttp.dao.model.dto;


import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.mview.db1.OrgNotaryInfoView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 *
 * @author Sang
 */
public class FileOrgNotary {
    
    private OrgNotaryInfoView orgNotaryInfoView;

    @Schema(description = "list các ccv đang hành nghề tại vpcc này")
    private PagingResult pageNotary;
    private PagingResult pageCCARegistration;

    @Schema(description = "list các thông tin xử phạt vpcc")
    private PagingResult pagePenalyze;

    @Schema(description = "list các thông tin chuyển nhượng")
    private PagingResult pageTransferOffice;

    @Schema(description = "lịch sử tổ chức")
    private PagingResult pageHistory;
    private List<OrgNotaryInfoView> listEstablish;
    private List<OrgNotaryInfoView> listRegis;
    private OrgNotaryInfoView detailGroup;
    private OrgNotaryInfoView detailVPGroup;
    private OrgNotaryInfoView detailMerger;
    private OrgNotaryInfoView detailVPMerger;
    private OrgNotaryInfoView detailTermination;
    private OrgNotaryInfoView detailEstablishPCC;
    private OrgNotaryInfoView detailConversion;
    private OrgNotaryInfoView detailDissolution;
    private OrgNotaryInfoView detailTransfer;

    public OrgNotaryInfoView getDetailVPGroup() {
        return detailVPGroup;
    }

    public void setDetailVPGroup(OrgNotaryInfoView detailVPGroup) {
        this.detailVPGroup = detailVPGroup;
    }
    
    public PagingResult getPageTransferOffice() {
        return pageTransferOffice;
    }

    public OrgNotaryInfoView getDetailVPMerger() {
        return detailVPMerger;
    }

    public void setDetailVPMerger(OrgNotaryInfoView detailVPMerger) {
        this.detailVPMerger = detailVPMerger;
    }

    public void setPageTransferOffice(PagingResult pageTransferOffice) {
        this.pageTransferOffice = pageTransferOffice;
    }
    
    public OrgNotaryInfoView getOrgNotaryInfoView() {
        return orgNotaryInfoView;
    }

    public void setOrgNotaryInfoView(OrgNotaryInfoView orgNotaryInfoView) {
        this.orgNotaryInfoView = orgNotaryInfoView;
    }

    public PagingResult getPageNotary() {
        return pageNotary;
    }

    public void setPageNotary(PagingResult pageNotary) {
        this.pageNotary = pageNotary;
    }

    public PagingResult getPageCCARegistration() {
        return pageCCARegistration;
    }

    public void setPageCCARegistration(PagingResult pageCCARegistration) {
        this.pageCCARegistration = pageCCARegistration;
    }

    public PagingResult getPagePenalyze() {
        return pagePenalyze;
    }

    public void setPagePenalyze(PagingResult pagePenalyze) {
        this.pagePenalyze = pagePenalyze;
    }

    public PagingResult getPageHistory() {
        return pageHistory;
    }

    public void setPageHistory(PagingResult pageHistory) {
        this.pageHistory = pageHistory;
    }

    public List<OrgNotaryInfoView> getListEstablish() {
        return listEstablish;
    }

    public void setListEstablish(List<OrgNotaryInfoView> listEstablish) {
        this.listEstablish = listEstablish;
    }

    public List<OrgNotaryInfoView> getListRegis() {
        return listRegis;
    }

    public void setListRegis(List<OrgNotaryInfoView> listRegis) {
        this.listRegis = listRegis;
    }

    public OrgNotaryInfoView getDetailGroup() {
        return detailGroup;
    }

    public void setDetailGroup(OrgNotaryInfoView detailGroup) {
        this.detailGroup = detailGroup;
    }

    public OrgNotaryInfoView getDetailMerger() {
        return detailMerger;
    }

    public void setDetailMerger(OrgNotaryInfoView detailMerger) {
        this.detailMerger = detailMerger;
    }

    public OrgNotaryInfoView getDetailTermination() {
        return detailTermination;
    }

    public void setDetailTermination(OrgNotaryInfoView detailTermination) {
        this.detailTermination = detailTermination;
    }

    public OrgNotaryInfoView getDetailEstablishPCC() {
        return detailEstablishPCC;
    }

    public void setDetailEstablishPCC(OrgNotaryInfoView detailEstablishPCC) {
        this.detailEstablishPCC = detailEstablishPCC;
    }

    public OrgNotaryInfoView getDetailConversion() {
        return detailConversion;
    }

    public void setDetailConversion(OrgNotaryInfoView detailConversion) {
        this.detailConversion = detailConversion;
    }

    public OrgNotaryInfoView getDetailDissolution() {
        return detailDissolution;
    }

    public void setDetailDissolution(OrgNotaryInfoView detailDissolution) {
        this.detailDissolution = detailDissolution;
    }

    public OrgNotaryInfoView getDetailTransfer() {
        return detailTransfer;
    }

    public void setDetailTransfer(OrgNotaryInfoView detailTransfer) {
        this.detailTransfer = detailTransfer;
    }
    
}
