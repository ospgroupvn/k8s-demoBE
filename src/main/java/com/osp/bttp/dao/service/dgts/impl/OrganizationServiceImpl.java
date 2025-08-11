package com.osp.bttp.dao.service.dgts.impl;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.entity.db2.AuDepOfJustice;
import com.osp.bttp.dao.service.dgts.OrganizationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sangnk
 * @Created 10/10/2024 - 11:32 SA
 * @project = bttp
 * @_ Mô tả:
 * Service dùng database đấu giá tài sản
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrganizationServiceImpl implements OrganizationService {
    @PersistenceContext(unitName = "db2")
    private EntityManager entityManager;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;

    @Override
    public ResponseEntity<ApiResponseV1<?>> getAllOrganization() {
        String keyRedis = "getAllOrganization";
        if ( redisEnable && redisTemplate.hasKey(keyRedis)) {
            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", redisTemplate.opsForValue().get(keyRedis)));
        }
        List<HashMap<String, Object>> result = new ArrayList<>();
        try {
            String query = "Select o.id, o.fullName, o.licenseNo, o.licenseDate, o.phoneNumber, o.cityId, ac.name, ac.code  from AuDepOfJustice o left join AuCategory ac on o.cityId = ac.id where o.status <> 0 order by ac.name ASC ";
            List<Object[]> dataList  = entityManager.createQuery(query).getResultList();
            for (Object[] objects : dataList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", objects[0]);
                map.put("fullName", objects[6]);
                map.put("licenseNo", objects[2]);
                map.put("licenseDate", objects[3]);
                map.put("phoneNumber", objects[4]);
                map.put("cityId", objects[5]);
                map.put("cityName", objects[6]);
                map.put("cityCode", objects[7]);
                result.add(map);
            }
            //cache 2h
            if ( redisEnable ) redisTemplate.opsForValue().set(keyRedis, result, 10, TimeUnit.MINUTES);
            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null));
        }
    }
}
