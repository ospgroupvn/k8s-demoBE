package com.osp.bttp.dao.model.mapper.db4;

import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerAssoc;
import com.osp.bttp.dao.model.dto.response.db4.GetListOrg;
import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.model.entity.db4.LOrganization;

public class LstcMapper {
    public static GetListLawyerAssoc toLawyerAssocDto(LLawyerAssociation lLawyerAssociation){
        GetListLawyerAssoc getListLawyerAssoc = new GetListLawyerAssoc();
        getListLawyerAssoc.setAssocId(lLawyerAssociation.getAssocId());
        getListLawyerAssoc.setAssocName(lLawyerAssociation.getAssocName());
        return  getListLawyerAssoc;
    }

    public static GetListOrg toLOrgDto(LOrganization lOrganization){
        GetListOrg getListOrg = new GetListOrg();
        getListOrg.setId(lOrganization.getOrgId());
        getListOrg.setOrgName(lOrganization.getOrgName());
        return getListOrg;
    }



}


