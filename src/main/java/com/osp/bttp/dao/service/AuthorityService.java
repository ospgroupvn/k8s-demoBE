package com.osp.bttp.dao.service;

import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.entity.db3.Authority;

import java.util.List;
import java.util.Optional;

/**
 * @author sangnk
 * @Created 09/10/2024 - 2:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface AuthorityService {
    public Optional<PagingResult> page(PagingResult page, String authKey, String type);

    public boolean addAuthority(Authority authoItem);

    public boolean isExits(Authority authoItem);

    public boolean editAuthority(Authority authoItem);

    public Authority getAuthorityById(Long authid);

    public boolean deleteAuthority(Authority authorityDel);

    public boolean checkAuthorityAssigned(long authid);

    public List<Authority> getListAuthParent(long authid, String type);

    public List<Authority> getAuthorityChildrenById(long id);
}
