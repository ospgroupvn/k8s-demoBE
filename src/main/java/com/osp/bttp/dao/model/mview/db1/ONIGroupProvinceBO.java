/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db1;

import java.util.List;

public class ONIGroupProvinceBO {
    private String provinceName;
    private List<PublicNotaryGroupProvinceBO> publicNotaryGroupProvinceBOS;

    public ONIGroupProvinceBO() {
    }

    public ONIGroupProvinceBO(String provinceName, List<PublicNotaryGroupProvinceBO> publicNotaryGroupProvinceBOS) {
        this.provinceName = provinceName;
        this.publicNotaryGroupProvinceBOS = publicNotaryGroupProvinceBOS;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<PublicNotaryGroupProvinceBO> getPublicNotaryGroupProvinceBOS() {
        return publicNotaryGroupProvinceBOS;
    }

    public void setPublicNotaryGroupProvinceBOS(List<PublicNotaryGroupProvinceBO> publicNotaryGroupProvinceBOS) {
        this.publicNotaryGroupProvinceBOS = publicNotaryGroupProvinceBOS;
    }
}
