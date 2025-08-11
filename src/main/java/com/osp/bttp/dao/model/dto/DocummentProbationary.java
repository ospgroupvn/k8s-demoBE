package com.osp.bttp.dao.model.dto;


import com.osp.bttp.dao.model.entity.db1.*;

public class DocummentProbationary {

    private DmDocument document;
    private ProbationaryInfo probationaryInfo;

    public DocummentProbationary() {
    }

    public DmDocument getDocument() {
        return document;
    }

    public void setDocument(DmDocument document) {
        this.document = document;
    }

    public ProbationaryInfo getProbationaryInfo() {
        return probationaryInfo;
    }

    public void setProbationaryInfo(ProbationaryInfo probationaryInfo) {
        this.probationaryInfo = probationaryInfo;
    }
}
