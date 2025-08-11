package com.osp.bttp.dao.model.mview.db1;

import lombok.Data;

import java.util.List;

@Data
public class TimeLineView {

    private String ten_chinh;
    private String administration;
    private String org_name;
    private String date_start;
    private String date_end;
    private String note;
    private String dispatch_code;
    private String date_sign;
    private String effective_date;
    private String number_cad;
    private String tabs;

    private List<String> body;

    public String getTen_chinh() {
        return ten_chinh;
    }

    public void setTen_chinh(String ten_chinh) {
        this.ten_chinh = ten_chinh;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDispatch_code() {
        return dispatch_code;
    }

    public void setDispatch_code(String dispatch_code) {
        this.dispatch_code = dispatch_code;
    }

    public String getDate_sign() {
        return date_sign;
    }

    public void setDate_sign(String date_sign) {
        this.date_sign = date_sign;
    }

    public String getEffective_date() {
        return effective_date;
    }

    public void setEffective_date(String effective_date) {
        this.effective_date = effective_date;
    }

    public String getNumber_cad() {
        return number_cad;
    }

    public void setNumber_cad(String number_cad) {
        this.number_cad = number_cad;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }
}
