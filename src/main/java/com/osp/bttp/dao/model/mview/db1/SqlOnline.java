package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.dto.PagingResult;

public class SqlOnline {
    private int type;
    private int typeQuery;
    private String colSelect;
    private String queryString;
    private String queryCount;
    private String exception;
    private String success;
    private int offset;
    private int number;
    private PagingResult page;
    private boolean error = false;
    private String message;

    public SqlOnline() {
    }

    public SqlOnline(String exception, String success) {
        this.exception = exception;
        this.success = success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeQuery() {
        return typeQuery;
    }

    public void setTypeQuery(int typeQuery) {
        this.typeQuery = typeQuery;
    }

    public String getColSelect() {
        return colSelect;
    }

    public void setColSelect(String colSelect) {
        this.colSelect = colSelect;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(String queryCount) {
        this.queryCount = queryCount;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PagingResult getPage() {
        return page;
    }

    public void setPage(PagingResult page) {
        this.page = page;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
