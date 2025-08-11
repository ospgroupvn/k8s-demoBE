package com.osp.bttp.dao.model.entity.base;

import java.util.Date;

public interface Creatable extends Updatable {

    String getCreateBy();
    Long getCreateById();
    Date getGenDate();

    void setCreateBy(String createBy);
    void setCreateById(Long createById);
    void setGenDate(Date genDate);
}
