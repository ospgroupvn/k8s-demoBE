package com.osp.bttp.dao.model.dto;


import com.osp.bttp.dao.model.entity.db2.*;
import com.osp.bttp.dao.model.mview.FileUpload;

import java.util.List;

public class CenterOrganizationAuctionner {
    public List<OrganizationHis> listOrganizationHises;
    public Organization organization;
    public List<Auctioneer> auctioneers;
    public List<AuMemberParter> ltsMemberParters;
    public Auctioneer manager;
    public List<Organization>ltsToChucHNSN;
    public Organization toChucDuocSN;
    private List<FileUpload> listFile;
    public List<Organization>listCNvaVP;



    public Organization getToChucDuocSN() {
        return toChucDuocSN;
    }

    public void setToChucDuocSN(Organization toChucDuocSN) {
        this.toChucDuocSN = toChucDuocSN;
    }
    public CenterOrganizationAuctionner() {
    }

    public CenterOrganizationAuctionner(Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Auctioneer> getAuctioneers() {
        return auctioneers;
    }

    public void setAuctioneers(List<Auctioneer> auctioneers) {
        this.auctioneers = auctioneers;
    }

    public Auctioneer getManager() {
        return manager;
    }

    public void setManager(Auctioneer manager) {
        this.manager = manager;
    }

    public List<Organization> getListCNvaVP() {
        return listCNvaVP;
    }

    public void setListCNvaVP(List<Organization> listCNvaVP) {
        this.listCNvaVP = listCNvaVP;
    }

    public List<Organization> getLtsToChucHNSN() {
        return ltsToChucHNSN;
    }

    public void setLtsToChucHNSN(List<Organization> ltsToChucHNSN) {
        this.ltsToChucHNSN = ltsToChucHNSN;
    }

    public List<OrganizationHis> getListOrganizationHises() {
        return listOrganizationHises;
    }

    public void setListOrganizationHises(List<OrganizationHis> listOrganizationHises) {
        this.listOrganizationHises = listOrganizationHises;
    }

    public List<FileUpload> getListFile() {
        return listFile;
    }

    public void setListFile(List<FileUpload> listFile) {
        this.listFile = listFile;
    }

    public List<AuMemberParter> getLtsMemberParters() {
        return ltsMemberParters;
    }

    public void setLtsMemberParters(List<AuMemberParter> ltsMemberParters) {
        this.ltsMemberParters = ltsMemberParters;
    }
}
