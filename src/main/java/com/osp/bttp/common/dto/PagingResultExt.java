/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class PagingResultExt extends PagingResult {

    public PagingResultExt() {
    }

    private List<?> itemsExt = new ArrayList();

    public List<?> getItemsExt() {
        return itemsExt;
    }

    public void setItemsExt(List<?> itemsExt) {
        this.itemsExt = itemsExt;
    }
}
