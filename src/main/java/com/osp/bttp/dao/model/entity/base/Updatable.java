package com.osp.bttp.dao.model.entity.base;

import java.util.Date;

public interface Updatable {
    String getUpdateBy();
    Long getUpdateById();
    Date getLastUpdated();
    void setUpdateBy(String updateBy);
    void setUpdateById(Long updateById);
    void setLastUpdated(Date lastUpdated);
}
