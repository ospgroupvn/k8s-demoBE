/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db1;

import java.util.List;


public class PublicNotaryGroupProvinceBO {
    private String orgNotaryInfo;
    private String provinceName;
    private List<NotaryInfoView> notaryInfoViews;
    private List<String> orgNotaryInfos;
    
    public PublicNotaryGroupProvinceBO() {
    }

    public PublicNotaryGroupProvinceBO(String orgNotaryInfo, String provinceName, List<NotaryInfoView> notaryInfoViews) {
        this.orgNotaryInfo = orgNotaryInfo;
        this.provinceName = provinceName;
        this.notaryInfoViews = notaryInfoViews;
    }

    public List<String> getOrgNotaryInfos() {
        return orgNotaryInfos;
    }

    public void setOrgNotaryInfos(List<String> orgNotaryInfos) {
        this.orgNotaryInfos = orgNotaryInfos;
    }

    
    public String getOrgNotaryInfo() {
        return orgNotaryInfo;
    }

    public void setOrgNotaryInfo(String orgNotaryInfo) {
        this.orgNotaryInfo = orgNotaryInfo;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<NotaryInfoView> getNotaryInfoViews() {
        return notaryInfoViews;
    }

    public void setNotaryInfoViews(List<NotaryInfoView> notaryInfoViews) {
        this.notaryInfoViews = notaryInfoViews;
    }
    
    
}
