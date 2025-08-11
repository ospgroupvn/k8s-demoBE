
package com.osp.bttp.dao.model.mview.db1;


public class DocumentType {
    private String name;
    private Long type;

    public DocumentType(String name, Long type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

   
    
}
