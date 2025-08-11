package com.osp.bttp.common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 12/26/2017.
 */
@Data
public class PagingResult<T> {

    private List<T> items = new ArrayList();
    private long rowCount = 0;
    private int numberPerPage = 25;
    private int pageNumber = 1;
    private byte checkLast = 0; // 1: trang cuối - 0: ko phải trang cuối
    private HashMap<String, Object> dtx = new HashMap();
    private int pageCount;
    private List<Integer> pageList;

    public PagingResult() {
    }

    public PagingResult(Page page) {
        items = page.getContent();
        rowCount = page.getTotalElements();
        pageNumber = page.getPageable().getPageNumber() + 1;
        numberPerPage = page.getPageable().getPageSize();
    }

    public List<Integer> getPageList() {
        List<Integer> pages = new ArrayList();
        int from = this.pageNumber - 4;
        int to = this.pageNumber + 7;
        if (from < 0) {
            to -= from;
            from = 1;
        }

        if (from < 1) {
            from = 1;
        }

        if (to > this.getPageCount()) {
            to = this.getPageCount();
        }

        for (int i = from; i <= to; ++i) {
            pages.add(Integer.valueOf(i));
        }

        return pages;
    }

    //    public int getPageCount() {
//        return (int)(Math.ceil((double)this.rowCount / (double)this.numberPerPage) + 1.0D);
//    }
    public int getPageCount() {
        return (int) (Math.ceil((double) this.rowCount / (double) this.numberPerPage));
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public int getNumberPerPage() {
        return this.numberPerPage;
    }

    public void setNumberPerPage(int numberPerPage) {
        this.numberPerPage = numberPerPage;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public byte getCheckLast() {
        return checkLast;
    }

    public void setCheckLast(byte checkLast) {
        this.checkLast = checkLast;
    }

}
