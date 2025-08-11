package com.osp.bttp.dao.model.mview.db1;

public class NotaryStatus {
    private String name;
    private Long status;

    public NotaryStatus(String name, Long status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
