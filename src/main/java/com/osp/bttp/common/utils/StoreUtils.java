package com.osp.bttp.common.utils;


import com.osp.bttp.dao.model.entity.base.Creatable;
import com.osp.bttp.dao.model.entity.base.Updatable;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Service
public class StoreUtils {


    /**
     * @param repository
     * @param model
     */
    public <DomainType extends Creatable, IDFieldType extends Serializable> DomainType save(
            JpaRepository<DomainType, IDFieldType> repository, DomainType model) {
        populateForSave(model);
        return repository.save(model);
    }
    public <DomainType extends Creatable, IDFieldType extends Serializable> void saveAndFlush(
            JpaRepository<DomainType, IDFieldType> repository, DomainType model) {

        populateForSave(model);
        repository.saveAndFlush(model);
    }

    public <DomainType extends Creatable, IDFieldType extends Serializable> void save(
            JpaRepository<DomainType, IDFieldType> repository, List<DomainType> models) {
        H.each(models, (index, model) -> populateForSave(model));
        repository.saveAll(models);
    }

    private void populateForSave(Creatable model) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.setGenDate(Calendar.getInstance().getTime());
        model.setCreateById(userLogin.getId());
        model.setCreateBy(userLogin.getFullName());
        this.populateForUpdate(model);
    }

    public <DomainType extends Creatable, IDFieldType extends Serializable> void update(
            JpaRepository<DomainType, IDFieldType> repository, List<DomainType> models) {
        H.each(models, (index, model) -> populateForUpdate(model));
        repository.saveAll(models);
    }

    /**
     * @param repository
     * @param model
     */
    public <DomainType extends Creatable, IDFieldType extends Serializable> DomainType update(
            JpaRepository<DomainType, IDFieldType> repository, DomainType model) {

        populateForUpdate(model);
        return repository.save(model);
    }

    private void populateForUpdate(Updatable model) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.setLastUpdated(Calendar.getInstance().getTime());
        model.setUpdateById(userLogin.getId());
        model.setUpdateBy(userLogin.getFullName());
    }





}
